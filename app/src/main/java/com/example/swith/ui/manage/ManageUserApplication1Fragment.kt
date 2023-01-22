package com.example.swith.ui.manage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swith.R
import com.example.swith.data.api.RetrofitService
import com.example.swith.databinding.FragmentManageApplicationBinding
import com.example.swith.domain.entity.ManageUserResponse
import com.example.swith.domain.entity.ManageUserResult
import com.example.swith.ui.adapter.ManageUserRVAdapter1
import com.example.swith.utils.base.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 신청서를 낸 사람들 목록 (지원)
class ManageUserApplication1Fragment() :
    BaseFragment<FragmentManageApplicationBinding>(R.layout.fragment_manage_application) {

    var groupIdx: Long? = -1
    var userActivity: ManageUserActivity? = null
    var status: Int = 0
    var resumeContent: String = ""
    var resumeIdx: Int? = -1
    private var adapter = ManageUserRVAdapter1()
    lateinit var userList: List<ManageUserResult>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        groupIdx = userActivity?.groupIdx
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setVisiblebar(false, true, "", "")
        binding.flLoadingLayout.visibility = View.VISIBLE
        setRetrofitData(groupIdx?.toLong())
    }

    fun setRetrofitData(groupIdx: Long?) {
        Log.e("groupIdx 레트로핏 값 ", "${groupIdx}")
        val retrofitService =
            RetrofitService.retrofit.create(com.example.swith.data.api.SwithService::class.java)
        //status가 0이면 지원했는데 아직 승인 안된사람들
        retrofitService.getUserList(groupIdx!!, 0).enqueue(object :
            Callback<ManageUserResponse> {
            override fun onResponse(
                call: Call<ManageUserResponse>,
                response: Response<ManageUserResponse>,
            ) {
                if (response.isSuccessful) {
                    Log.e("summer", "성공${response.toString()}")
                    Log.e("유저들 정보", "${response.body()?.result.toString()}")
                    response.body()?.apply {
                        initRV(this.result)
                        binding.flLoadingLayout.visibility = View.GONE
                    }
                    if (response.body() == null) {
                        binding.flLoadingLayout.visibility = View.VISIBLE
                    }
                } else {
                    Log.e("summer", "전달실패 code = ${response.code()}")
                    Log.e("summer", "전달실패 msg = ${response.message()}")
                }
            }

            override fun onFailure(
                call: Call<ManageUserResponse>,
                t: Throwable,
            ) {
                Log.e("summer", "onFailure t = ${t.toString()}")
                Log.e("summer", "onFailure msg = ${t.message}")
            }
        })
    }

    fun initRV(userList: List<ManageUserResult>) {
        val adapter = ManageUserRVAdapter1()
        adapter.userList.addAll(userList)
        binding.rvApplication.adapter = adapter
        binding.rvApplication.setLayoutManager(LinearLayoutManager(getActivity()))

        adapter.setItemClickListener(object : ManageUserRVAdapter1.OnItemClickListener {
            override fun onClick(view: View, pos: Int, userIdx: Long?) {
                Log.e("클릭이벤트 발생", "true")
                Log.e("userIdx 값", "${userIdx}")
                val intent = Intent(requireActivity(), ManageUserProfileActivity::class.java)
                intent.putExtra("userIdx", userIdx)
                startActivity(intent)
            }

            override fun resumeClick(
                v: View,
                pos: Int,
                applicationContent: String?,
                applicationIdx: Long?,
            ) {
                Log.e("frag1 resume 클릭 이벤트", "true")
                val intent = Intent(requireActivity(), ManageUserResumeActivity::class.java)
                intent.putExtra("status", status)
                intent.putExtra("groupIdx", groupIdx)
                intent.putExtra("applicationContent", applicationContent)
                intent.putExtra("applicationIdx", applicationIdx)
                startActivity(intent)
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userActivity = activity as ManageUserActivity
    }

    override fun onResume() {
        super.onResume()
    }

}