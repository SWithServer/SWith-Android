package com.example.swith.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.swith.R
import com.example.swith.data.Study
import com.example.swith.databinding.FragmentHomeBinding
import com.example.swith.ui.study.StudyActivity

class HomeFragment : Fragment(){
    lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val adapter = HomeStudyRVAdapter()
        binding.homeStudyRv.adapter = adapter
        adapter.setMyItemClickListener(object: HomeStudyRVAdapter.myItemClickListener{
            override fun onItemClick(study: Study) {
                val intent = Intent(activity, StudyActivity::class.java)
                startActivity(intent)
            }
        })

        // 스터디 존재하면 RecyclerView에 추가
        // StudyList 받아와야 할 부분
        // ViewModel 내부에서 Repository로부터 받아와야하는 부분?!
        // 임시로 그냥 만들음
        val studyList = ArrayList<Study>()
        studyList.add(Study("영어 스터디", "회화", 8, 5))
        studyList.add(Study("자격증 뿌시자", "자격증", 7, 3))
        viewModel.initData(studyList)

        viewModel.studyLiveData.observe(viewLifecycleOwner, Observer{
            (binding.homeStudyRv.adapter as HomeStudyRVAdapter).setData(it)
        })

        binding.homeStudyAddIv.setOnClickListener{
            // 기능 확인을 위해 누르면 스터디 하나 추가되게 함
            viewModel.addStudy(Study("스터디 2", "스터디", 3, 2))
        }

        return binding.root
    }
}