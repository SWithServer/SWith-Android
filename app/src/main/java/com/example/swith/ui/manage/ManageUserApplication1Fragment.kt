package com.example.swith.ui.manage

import android.content.Context
import android.content.Intent
import android.graphics.Insets.add
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.data.GetSessionRes
import com.example.swith.data.ManageUserResult
import com.example.swith.databinding.ActivityManageUserResumeBinding
import com.example.swith.databinding.FragmentManageApplicationBinding
import com.example.swith.databinding.ItemMangeRoundBinding
import com.example.swith.ui.MainActivity
import com.example.swith.ui.adapter.ManageUserRVAdapter1
import com.example.swith.ui.adapter.StudyFindRVAdapter
import com.example.swith.ui.study.create.SelectPlaceActivity
import com.example.swith.ui.study.find.StudyFindDetailFragment
import com.example.swith.utils.ItemTouchHelperListener
import com.example.swith.utils.base.BaseFragment

// 신청서를 낸 사람들 목록 (지원)
class ManageUserApplication1Fragment() :BaseFragment<FragmentManageApplicationBinding>(R.layout.fragment_manage_application) {

    var groupIdx : Int?  = -1
   var userActivity : ManageUserActivity? =null
    private lateinit var adapter : ManageUserRVAdapter1
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
        adapter = ManageUserRVAdapter1()
        binding.rvApplication.adapter = adapter
        setData()
        binding.rvApplication.apply{
            loadData()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        adapter.setItemClickListener(object:ManageUserRVAdapter1.OnItemClickListener{
            override fun onClick(view: View, pos:Int,userIdx:Long?) {
                Log.e("클릭이벤트 발생","true")
                Log.e("userIdx 값","${userIdx}")
                val intent = Intent(requireActivity(), ManageUserProfileActivity::class.java)
                intent.putExtra("userIdx", userIdx)
                startActivity(intent)
            }
        })
    }

    fun loadData()
    {
        adapter.setUser(userList)
    }

    fun setData()
    {
        userList = ArrayList<ManageUserResult>()
        Log.e("groupIdx 데이터로드", "${groupIdx}")
        userList.apply{
            add(ManageUserResult(1,"유저1", "https://lh3.googleusercontent.com/a/AItbvmmZTEhJKpZdLsPHSnT9XH2q469L0kulNTIFqjm2=s96-c",1,"화이팅"))
            add(ManageUserResult(2,"유저2", "https://lh3.googleusercontent.com/a/AItbvmmZTEhJKpZdLsPHSnT9XH2q469L0kulNTIFqjm2=s96-c",2,"아자아자"))
            add(ManageUserResult(3,"유저3", "https://lh3.googleusercontent.com/a/AItbvmmZTEhJKpZdLsPHSnT9XH2q469L0kulNTIFqjm2=s96-c",3,"열심히할게요"))
            add(ManageUserResult(4,"유저4", "https://lh3.googleusercontent.com/a/AItbvmmZTEhJKpZdLsPHSnT9XH2q469L0kulNTIFqjm2=s96-c",4,"스터디 화이팅"))
            add(ManageUserResult(5,"유저5", "https://lh3.googleusercontent.com/a/AItbvmmZTEhJKpZdLsPHSnT9XH2q469L0kulNTIFqjm2=s96-c",5,"야호~"))
            add(ManageUserResult(6,"유저6", "https://lh3.googleusercontent.com/a/AItbvmmZTEhJKpZdLsPHSnT9XH2q469L0kulNTIFqjm2=s96-c",6,"짱"))
            add(ManageUserResult(7,"유저7", "https://lh3.googleusercontent.com/a/AItbvmmZTEhJKpZdLsPHSnT9XH2q469L0kulNTIFqjm2=s96-c",7,"열시미"))
            add(ManageUserResult(8,"유저8", "https://lh3.googleusercontent.com/a/AItbvmmZTEhJKpZdLsPHSnT9XH2q469L0kulNTIFqjm2=s96-c",8,"열시미"))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userActivity = activity as ManageUserActivity
    }
}