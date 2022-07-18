package com.example.swith.ui.study

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.swith.R
import com.example.swith.data.Round
import com.example.swith.databinding.FragmentRoundBinding
import com.example.swith.ui.adapter.RoundRVAdapter
import com.example.swith.ui.study.create.CreateRoundActivity
import com.example.swith.ui.study.notice.NoticeActivity
import com.example.swith.viewmodel.RoundViewModel

class RoundFragment : Fragment() {
    lateinit var binding: FragmentRoundBinding
    private val viewModel: RoundViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_round, container, false)

        binding.roundListRv.apply {
            adapter = RoundRVAdapter()
        }

        initListener()

        viewModel.roundLiveData.observe(viewLifecycleOwner, Observer {
            (binding.roundListRv.adapter as RoundRVAdapter).setData(it)
        })

        return binding.root
    }
    private fun initListener(){
        var testCount = 7
        with(binding){
            roundAddBtn.setOnClickListener { viewModel.addData(Round(testCount, "22/7/14", "22/7/16", "영어 ${testCount++}회차 스터디", true, null)) }
            roundNoticeIv.setOnClickListener { startActivity(Intent(activity, NoticeActivity::class.java)) }
            roundAddBtn.setOnClickListener { startActivity(Intent(activity, CreateRoundActivity::class.java)) }
            roundPreviousCb.setOnCheckedChangeListener { view, isChecked -> viewModel.setPastData(view.isChecked) }
        }
    }

}