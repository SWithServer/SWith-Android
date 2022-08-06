package com.example.swith.ui.study.round

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.swith.R
import com.example.swith.data.DateTime
import com.example.swith.data.GetSessionRes
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
        setViewVisibility(true)
        viewModel.loadData()

        binding.roundListRv.apply {
            adapter = RoundRVAdapter().apply {
                setItemClickListener(object : RoundRVAdapter.myItemClickListener {
                    override fun onItemClick(round: GetSessionRes) {
                        viewModel.setCurrentData(round)
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.study_frm, RoundTabFragment())
                            .commitAllowingStateLoss()
                    }
                })
            }
        }
        viewModel.roundLiveData.observe(viewLifecycleOwner, Observer {
            setViewVisibility(false)
            (binding.roundListRv.adapter as RoundRVAdapter).setData(it)
            binding.roundNoticeContentTv.text = it.announcementContent
        })

        initListener()
    }

    private fun setViewVisibility(beforeDataLoad: Boolean){
        with(binding) {
            if (beforeDataLoad) {
                roundCircularIndicator.visibility = View.VISIBLE
                roundMainLayout.visibility = View.INVISIBLE
            }else{
                roundMainLayout.visibility = View.VISIBLE
                roundCircularIndicator.visibility = View.INVISIBLE
            }
        }
    }

    private fun initListener(){
        with(binding){
            roundNoticeIv.setOnClickListener { startActivity(Intent(activity, NoticeActivity::class.java)) }
            roundAddBtn.setOnClickListener { startActivity(Intent(activity, RoundCreateActivity::class.java)) }
            roundPreviousCb.setOnCheckedChangeListener { view, isChecked -> viewModel.setPastData(view.isChecked) }
        }
    }

}