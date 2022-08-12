package com.example.swith.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swith.R
import com.example.swith.data.Group
import com.example.swith.data.GroupRV
import com.example.swith.databinding.FragmentHomeBinding
import com.example.swith.utils.base.BaseFragment
import com.example.swith.ui.adapter.HomeStudyRVAdapter
import com.example.swith.ui.study.StudyActivity
import com.example.swith.ui.study.create.StudyCreateActivity
import com.example.swith.viewmodel.HomeViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        binding.homeStudyRv.apply {
            adapter = HomeStudyRVAdapter().apply {
                setMyItemClickListener(object: HomeStudyRVAdapter.myItemClickListener{
                    override fun onItemClick(group: Group) {
                        startActivity(Intent(activity, StudyActivity::class.java).apply {
                            putExtra("group", group.groupIdx)
                        })
                    }
                })
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        binding.homeStudyAddIv.setOnClickListener{
            startActivity(Intent(requireActivity(), StudyCreateActivity::class.java))
        }
    }

    private fun observeViewModel(){
        // progress bar
        setViewVisibility(isStudyNotExists = true, beforeDataLoad = true)
        viewModel.loadData()

        viewModel.groupLiveData.observe(viewLifecycleOwner, Observer{ data ->
            // 스터디가 1개 이상 존재하면 스터디 리사이클러 뷰 보여줌
            data?.group.let{(binding.homeStudyRv.adapter as HomeStudyRVAdapter).setData(data)}
        })

        viewModel.mutableErrorMessage.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        })

        viewModel.mutableErrorType.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireActivity(), "$it 오류", Toast.LENGTH_SHORT).show()
        })

        viewModel.mutableScreenState.observe(viewLifecycleOwner, Observer {
            setViewVisibility(viewModel.getEmptyOrNull(), false)
        })
    }

    private fun setViewVisibility(isStudyNotExists: Boolean, beforeDataLoad: Boolean){
        with(binding) {
            if (beforeDataLoad){
                homeScrollviewSv.visibility = View.GONE
                homeNoStudyLayout.visibility = View.GONE
            }else if (isStudyNotExists) {
                homeScrollviewSv.visibility = View.GONE
                homeNoStudyLayout.visibility = View.VISIBLE
                homeCircularIndicator.visibility = View.GONE
            } else{
                homeScrollviewSv.visibility = View.VISIBLE
                homeNoStudyLayout.visibility = View.GONE
                homeCircularIndicator.visibility = View.GONE
            }
        }
    }
}