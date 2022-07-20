package com.example.swith.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.swith.R
import com.example.swith.data.Study
import com.example.swith.databinding.FragmentHomeBinding
import com.example.swith.ui.BaseFragment
import com.example.swith.ui.adapter.HomeStudyRVAdapter
import com.example.swith.ui.study.StudyActivity
import com.example.swith.ui.study.create.StudyCreateActivity
import com.example.swith.viewmodel.HomeViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeStudyRv.apply {
            adapter = HomeStudyRVAdapter().apply {
                setMyItemClickListener(object: HomeStudyRVAdapter.myItemClickListener{
                    override fun onItemClick(study: Study) {
                        startActivity(Intent(activity, StudyActivity::class.java))
                    }
                })
            }
        }

        viewModel.studyLiveData.observe(viewLifecycleOwner, Observer{
            // 스터디가 1개 이상 존재하면 스터디 리사이클러 뷰 보여줌
            setViewVisibility(it.isNotEmpty())
            (binding.homeStudyRv.adapter as HomeStudyRVAdapter).setData(it)
        })

        binding.homeStudyAddIv.setOnClickListener{
            startActivity(Intent(requireActivity(), StudyCreateActivity::class.java))
        }
    }

    private fun setViewVisibility(isStudyExists: Boolean){
        with(binding) {
            if (isStudyExists) {
                homeScrollviewSv.visibility = View.VISIBLE
                homeNoStudyLayout.visibility = View.GONE
            } else{
                homeScrollviewSv.visibility = View.GONE
                homeNoStudyLayout.visibility = View.VISIBLE
            }
        }
    }
}