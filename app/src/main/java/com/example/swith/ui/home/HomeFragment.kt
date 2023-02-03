package com.example.swith.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swith.R
import com.example.swith.databinding.FragmentHomeBinding
import com.example.swith.domain.entity.Group
import com.example.swith.ui.adapter.HomeStudyRVAdapter
import com.example.swith.ui.study.StudyActivity
import com.example.swith.ui.study.create.StudyCreateActivity
import com.example.swith.utils.UiText
import com.example.swith.utils.base.BaseFragment
import com.example.swith.utils.base.BaseState
import com.example.swith.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initLauncher()
        observeViewModel()
    }

    private fun initLauncher(){
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    viewModel.loadData()
                }
            }
    }

    private fun observeViewModel() {
        with(binding){
            lifecycleScope.launch{
                repeatOnLifecycle(Lifecycle.State.STARTED){
                    launch {
                        viewModel.groupData.collectLatest {
                            when(it){
                                is BaseState.Success -> {
                                    (homeStudyRv.adapter as HomeStudyRVAdapter).setData(it.value)
                                }
                                else -> {}
                            }
                        }
                    }
                    launch {
                        viewModel.errors.collectLatest {
                            when(it){
                                is UiText.DynamicString -> {
                                    showSnackBar(it.value)
                                }
                                else -> showSnackBar(it.asString(requireContext()))
                            }
                        }
                    }

                }
            }
        }
    }

    private fun initView(){
        binding.homeViewModel = viewModel
        binding.lifecycleOwner = this
        setVisiblebar(false, true, "", "")
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

    private fun showSnackBar(message: String){
        Snackbar.make(binding.homeScrollviewSv, message, Snackbar.LENGTH_SHORT)
            .setTextColor(resources.getColor(R.color.white, null))
            .show()
    }
}