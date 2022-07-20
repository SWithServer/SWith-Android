package com.example.swith.ui.study.round

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.swith.R
import com.example.swith.data.Round
import com.example.swith.databinding.FragmentRoundBinding
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
        binding.vpRoundTab.adapter = RoundTabVPAdapter(this)
        TabLayoutMediator(binding.tbLayoutRoundTab, binding.vpRoundTab){
            tab, position -> tab.text = information[position]
        }.attach()

    }
}