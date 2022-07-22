package com.example.swith.ui.study.round


import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.swith.R
import com.example.swith.data.Round
import com.example.swith.databinding.FragmentRoundSummaryBinding
import com.example.swith.ui.BaseFragment
import com.example.swith.viewmodel.RoundViewModel


class RoundSummaryFragment : BaseFragment<FragmentRoundSummaryBinding>(R.layout.fragment_round_summary) {
    private val viewModel: RoundViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView(){
        val curRound : Round? = viewModel.currentLiveData.value
        with(binding){
            tvSummaryCount.text = "${curRound?.count}회차"
            tvSummaryDate.text = "${curRound?.startTime} ~ ${curRound?.endTime}"
            tvSummaryContent.text = "학습 내용 : ${curRound?.detail}"
            tvSummaryPlace.text = String.format("장소 : %s", if(curRound?.place != null) curRound?.place else "온라인")
        }
    }
}