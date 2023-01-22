package com.example.swith.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.R
import com.example.data.databinding.FragmentHomeBinding
import com.example.data.ui.adapter.HomeStudyRVAdapter
import com.example.data.ui.study.StudyActivity
import com.example.data.ui.study.create.StudyCreateActivity
import com.example.data.utils.SharedPrefManager
import com.example.data.utils.base.BaseFragment
import com.example.data.viewmodel.HomeViewModel
import com.example.swith.domain.entity.Group

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //추가
        setVisiblebar(false, true, "", "")

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
                if (it.resultCode == Activity.RESULT_OK) {
                    setViewVisibility(isStudyNotExists = true, beforeDataLoad = true)
                    viewModel.loadData()
                }
            }

        observeViewModel()

        binding.homeStudyRv.apply {
            adapter = HomeStudyRVAdapter().apply {
                setMyItemClickListener(object : HomeStudyRVAdapter.myItemClickListener {
                    override fun onItemClick(group: Group) {
                        activityResultLauncher.launch(
                            Intent(
                                activity,
                                StudyActivity::class.java
                            ).apply {
                                putExtra("group", group.groupIdx)
                            })
                    }
                })
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        binding.homeStudyAddIv.setOnClickListener {
            Log.i(
                "doori",
                "shared = ${SharedPrefManager(requireActivity()).getLoginData().toString()}"
            )
            activityResultLauncher.launch(
                Intent(
                    requireActivity(),
                    StudyCreateActivity::class.java
                )
            )
        }

        binding.homePullToRefresh.apply {
            setOnRefreshListener {
                isRefreshing = false
                viewModel.loadData()
            }
            setColorSchemeResources(R.color.color_swith)
        }
    }

    private fun observeViewModel() {
        setViewVisibility(isStudyNotExists = true, beforeDataLoad = true)
        viewModel.loadData()

        viewModel.groupLiveData.observe(viewLifecycleOwner, Observer { data ->
            // 스터디가 1개 이상 존재하면 스터디 리사이클러 뷰 보여줌
            data?.group.let { (binding.homeStudyRv.adapter as HomeStudyRVAdapter).setData(data) }
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

    private fun setViewVisibility(isStudyNotExists: Boolean, beforeDataLoad: Boolean) {
        with(binding) {
            if (beforeDataLoad) {
                homeScrollviewSv.visibility = View.GONE
                homeNoStudyLayout.visibility = View.GONE
            } else if (isStudyNotExists) {
                homeScrollviewSv.visibility = View.GONE
                homeNoStudyLayout.visibility = View.VISIBLE
                homeCircularIndicator.visibility = View.GONE
            } else {
                homeScrollviewSv.visibility = View.VISIBLE
                homeNoStudyLayout.visibility = View.GONE
                homeCircularIndicator.visibility = View.GONE
            }
        }
    }
}