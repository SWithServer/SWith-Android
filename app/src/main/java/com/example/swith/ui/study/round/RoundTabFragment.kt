package com.example.swith.ui.study.round

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.swith.R
import com.example.swith.databinding.FragmentRoundTabBinding
import com.example.swith.ui.BaseFragment
import com.example.swith.ui.adapter.RoundTabVPAdapter
import com.example.swith.viewmodel.RoundViewModel
import com.google.android.material.tabs.TabLayoutMediator

class RoundTabFragment() : BaseFragment<FragmentRoundTabBinding>(R.layout.fragment_round_tab){
    // 각 회차를 눌렀을 때 탭 레이아웃 조정을 위한 프래그먼트
    private val information = arrayListOf<String>("개요", "출석", "메모")
    private val viewModel: RoundViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabLayout()
    }
    private fun initTabLayout(){
        binding.vpRoundTab.adapter = RoundTabVPAdapter(this, viewModel.currentLiveData?.value?.sessionNum!!)
        TabLayoutMediator(binding.tbLayoutRoundTab, binding.vpRoundTab){
            tab, position -> tab.text = information[position]
        }.attach()
    }
}