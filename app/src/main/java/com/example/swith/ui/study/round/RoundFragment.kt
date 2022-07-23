package com.example.swith.ui.study.round

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.swith.R
import com.example.swith.data.DateTime
import com.example.swith.data.Round
import com.example.swith.databinding.FragmentRoundBinding
import com.example.swith.ui.BaseFragment
import com.example.swith.ui.adapter.RoundRVAdapter
import com.example.swith.ui.study.create.RoundCreateActivity
import com.example.swith.ui.study.notice.NoticeActivity
import com.example.swith.viewmodel.RoundViewModel

class RoundFragment : BaseFragment<FragmentRoundBinding>(R.layout.fragment_round) {
    private val viewModel: RoundViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()

        binding.roundListRv.apply {
            adapter = RoundRVAdapter(viewModel.curCount).apply {
                setItemClickListener(object : RoundRVAdapter.myItemClickListener {
                    override fun onItemClick(round: Round) {
                        viewModel.setCurrentData(round)
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.study_frm, RoundTabFragment())
                            .commitAllowingStateLoss()
                    }
                })
            }
        }

        viewModel.roundLiveData.observe(viewLifecycleOwner, Observer {
            (binding.roundListRv.adapter as RoundRVAdapter).setData(it)
        })
    }

    private fun initListener(){
        var testCount = 7
        with(binding){
            roundAddBtn.setOnClickListener { viewModel.addData(Round(testCount, DateTime(2022, 7, 22, 22, 0), DateTime(2022, 7, 22, 23, 0), "영어 ${testCount++}회차 스터디", true, null, 3)) }
            roundNoticeIv.setOnClickListener { startActivity(Intent(activity, NoticeActivity::class.java)) }
            roundAddBtn.setOnClickListener { startActivity(Intent(activity, RoundCreateActivity::class.java)) }
            roundPreviousCb.setOnCheckedChangeListener { view, isChecked -> viewModel.setPastData(view.isChecked) }
        }
    }

}