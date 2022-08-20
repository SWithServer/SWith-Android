package com.example.swith.ui.study.round

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.swith.R
import com.example.swith.data.GetSessionRes
import com.example.swith.databinding.FragmentRoundBinding
import com.example.swith.utils.base.BaseFragment
import com.example.swith.ui.adapter.RoundRVAdapter
import com.example.swith.ui.study.StudyActivity
import com.example.swith.ui.study.create.RoundCreateActivity
import com.example.swith.ui.study.announce.AnnounceActivity
import com.example.swith.viewmodel.RoundViewModel

class RoundFragment : BaseFragment<FragmentRoundBinding>(R.layout.fragment_round) {
    private var pastVisible = false
    private val viewModel: RoundViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.roundListRv.apply {
            adapter = RoundRVAdapter().apply {
                setItemClickListener(object : RoundRVAdapter.myItemClickListener {
                    override fun onItemClick(round: GetSessionRes) {
                        viewModel.setCurrentData(round.sessionIdx)
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.study_frm, RoundTabFragment())
                            .commitAllowingStateLoss()
                    }
                })
            }
        }

        observeViewModel()

        initListener()
    }

    override fun onResume() {
        super.onResume()
        setViewVisibility(true)
        viewModel.loadData()
    }

    private fun observeViewModel(){
        viewModel.roundLiveData.observe(viewLifecycleOwner, Observer {
            (binding.roundListRv.adapter as RoundRVAdapter).setData(it)
            binding.roundNoticeContentTv.text = it.announcementContent
            setManagerLayout(it.admin)
        })

        viewModel.mutableScreenState.observe(viewLifecycleOwner, Observer {
            setViewVisibility(false)
        })

        viewModel.mutableErrorType.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireActivity(), "$it 오류", Toast.LENGTH_SHORT).show()
        })
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
            roundNoticeLayout.setOnClickListener { startActivity(Intent(activity, AnnounceActivity::class.java).apply { putExtra("manager", viewModel.roundLiveData.value?.admin)
                                                    putExtra("groupIdx", viewModel.groupIdx)})}
            roundAddBtn.setOnClickListener { startActivity(Intent(activity, RoundCreateActivity::class.java).apply { putExtra("minuteMin", viewModel.roundLiveData.value?.attendanceValidTime)
                                                    putExtra("groupIdx", viewModel.groupIdx)
            }) }
            roundPreviousTv.setOnClickListener {
                pastVisible = !pastVisible
                if (pastVisible){
                    roundPreviousTv.text = "지난 회차 안보기"
                    roundPreviousTv.setTextColor(resources.getColor(R.color.color_cdcdcd, null))
                } else {
                    roundPreviousTv.text = "지난 회차 보기"
                    roundPreviousTv.setTextColor(resources.getColor(R.color.color_ADA0FF, null))
                }
                viewModel.roundLiveData.value?.let { viewModel.setPastData(pastVisible) }
            }
            roundPullToRefresh.apply {
                setOnRefreshListener {
                    isRefreshing = false
                    viewModel.loadData()
                }
                setColorSchemeResources(R.color.color_swith)
            }
        }
    }

}