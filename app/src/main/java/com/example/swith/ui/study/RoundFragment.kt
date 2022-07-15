package com.example.swith.ui.study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swith.R
import com.example.swith.data.Round
import com.example.swith.databinding.FragmentRoundBinding

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

        // Check Box 선택 여부 리스너
        binding.roundPreviousCb.setOnCheckedChangeListener { view, isChecked ->
            viewModel.setPastData(view.isChecked)
        }

        // Add button 리스너
        var testCount = 7
        binding.roundAddBtn.setOnClickListener {
            viewModel.addData(Round(testCount, "22/7/14", "22/7/16", "영어 ${testCount++}회차 스터디", true, null))
        }

        // repository로 변경해야 할 부분
        // 임시로 여기다 추가, 실제론 viewmodel 에서 api를 통해 받아와야 함
        val roundData = ArrayList<Round>()
        roundData.add(Round(1, "22/7/12", "22/7/16", "영어 1회차 스터디", true, null))
        roundData.add(Round(2, "22/7/13", "22/7/16", "영어 2회차 스터디", true, null))
        roundData.add(Round(3, "22/7/14", "22/7/16", "영어 3회차 스터디", true, null))
        roundData.add(Round(4, "22/7/14", "22/7/16", "영어 4회차 스터디", true, null))
        roundData.add(Round(5, "22/7/14", "22/7/16", "영어 5회차 스터디", true, null))
        roundData.add(Round(6, "22/7/14", "22/7/16", "영어 6회차 스터디", true, null))
        viewModel.initData(roundData, 3)

        viewModel.roundLiveData.observe(viewLifecycleOwner, Observer {
            (binding.roundListRv.adapter as RoundRVAdapter).setData(it)
        })


        return binding.root
    }
}