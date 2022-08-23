package com.example.swith.ui.manage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat.apply
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swith.R
import com.example.swith.data.*
import com.example.swith.databinding.FragmentManageApplication2Binding
import com.example.swith.repository.RetrofitApi
import com.example.swith.repository.RetrofitService
import com.example.swith.ui.adapter.ManageRoundRVAdapter
import com.example.swith.ui.adapter.ManageUserRVAdapter1
import com.example.swith.ui.adapter.ManageUserRVAdapter2
import com.example.swith.ui.dialog.CustomConfirmDialog
import com.example.swith.utils.SharedPrefManager
import com.example.swith.utils.SwipeController
import com.example.swith.utils.base.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//신청서 불필요 (선착순 목록)
class ManageUserApplication2Fragment() : BaseFragment<FragmentManageApplication2Binding>(R.layout.fragment_manage_application2) {

    var groupIdx : Int?  = -1
    var userActivity : ManageUserActivity? =null
    var status :Int = 1
//    val adminId = SharedPrefManager(requireActivity()).getLoginData()
//    val adminIdx = adminId?.userIdx

    val adminIdx = 1

    private lateinit var adapter : ManageUserRVAdapter2
    lateinit var userList : ArrayList<ManageUserResult>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        groupIdx = userActivity?.groupIdx
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setVisiblebar(false,true,"")
        setRetrofitData(groupIdx?.toLong())
    }

    fun initRV(userList : List<ManageUserResult>)
    {
        val adapter = ManageUserRVAdapter2()
        adapter.userList.addAll(userList)
        binding.rvApplication.adapter = adapter
        binding.rvApplication.setLayoutManager(LinearLayoutManager(getActivity()))

        adapter.setItemClickListener(object:ManageUserRVAdapter2.OnItemClickListener{
            override fun onClick(view: View, pos:Int,userIdx:Long?) {
                Log.e("클릭이벤트 발생","true")
                Log.e("userIdx 값","${userIdx}")
                val intent = Intent(requireActivity(), ManageUserProfileActivity::class.java)
                intent.putExtra("userIdx", userIdx)
                startActivity(intent)
            }
            override fun resumeClick(v:View, pos:Int,applicationContent:String?) {
                Log.e("frag1 resume 클릭 이벤트","true")
                val intent = Intent(requireActivity(), ManageUserResumeActivity::class.java)
                intent.putExtra("status",status)
                intent.putExtra("groupIdx",groupIdx)
                intent.putExtra("applicationContent",applicationContent)
                startActivity(intent)
            }
        })
        adapter.setCustomListener(object: ManageUserRVAdapter2.CustomListener{
            override fun onDeleteClick(userInfo: ManageUserResult,applicationIdx:Long) {
                Log.e("스와이프 이벤트 발생","true")
                CustomConfirmDialog("회원 삭제", "${userInfo.nickname}님을 추방하시겠습니까?").apply {
                    setCustomListener(object: CustomConfirmDialog.CustomListener{
                        override fun onConfirm() {
                            dismiss()
                            deleteUser(userInfo.userIdx,adminIdx?.toLong(),applicationIdx)
                        }
                    })
                }.show(requireActivity().supportFragmentManager,"userDelete")
            }
        })
        val itemTouchHelper = ItemTouchHelper(SwipeController(adapter))
        itemTouchHelper.attachToRecyclerView(binding.rvApplication)
    }


    fun setRetrofitData(groupIdx:Long?)
    {
        Log.e("groupIdx 레트로핏 값 ", "${groupIdx}")
        val retrofitService = RetrofitService.retrofit.create(RetrofitApi::class.java)
        //status가 1이면 지원해서 승인된 사람 or 선착순으로 바로 통과된 사람
        retrofitService.getUserList(groupIdx!!,1).enqueue(object :
            Callback<ManageUserResponse> {
            override fun onResponse(
                call: Call<ManageUserResponse>,
                response: Response<ManageUserResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("summer", "성공${response.toString()}")
                    response.body()?.apply {
                        Log.e("user들 목록",this.result.toString())
                        initRV(this.result)
                    }
                }
                else {
                    Log.e("summer", "전달실패 code = ${response.code()}")
                    Log.e("summer", "전달실패 msg = ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ManageUserResponse>, t: Throwable) {
                Log.e("summer", "onFailure t = ${t.toString()}")
                Log.e("summer", "onFailure msg = ${t.message}")
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userActivity = activity as ManageUserActivity
    }

    fun deleteUser(userIdx : Long?,adminIdx:Long?,applicationIdx:Long?)
    {
        val retrofitService = RetrofitService.retrofit.create(RetrofitApi::class.java)
        //status가 1이면 지원해서 승인된 사람 or 선착순으로 바로 통과된 사람
        Log.e("전달한 applicationIdx 값",applicationIdx.toString())
        val deleteReq =ManageUserDelReq(adminIdx,userIdx,applicationIdx)
        retrofitService.deleteUser(groupIdx!!.toLong(),1,deleteReq).enqueue(object :
            Callback<ManageUserDelResponse> {
            override fun onResponse(
                call: Call<ManageUserDelResponse>,
                response: Response<ManageUserDelResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("summer", "성공${response.toString()}")
                    response.body()?.apply {
                        Log.e("삭제한 applicationIdx 값",this.applicationIdx.toString())
                        setRetrofitData(groupIdx?.toLong())
                    }
                }
                else {
                    Log.e("summer", "전달실패 code = ${response.code()}")
                    Log.e("summer", "전달실패 msg = ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ManageUserDelResponse>, t: Throwable) {
                Log.e("summer", "onFailure t = ${t.toString()}")
                Log.e("summer", "onFailure msg = ${t.message}")
            }
        })

    }
}