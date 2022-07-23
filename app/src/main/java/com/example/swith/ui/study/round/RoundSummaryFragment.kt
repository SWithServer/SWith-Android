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
        val curRound : Round = viewModel.currentLiveData.value!!
        with(binding){
            tvSummaryCount.text = "${curRound.count}회차"
            tvSummaryDate.text = with(curRound){
                // 시작 날짜와 종료 날짜가 같은 경우 시작 시간의 날짜만 표시, 종료 시간의 날짜는 표시 X
                if (startTime.year == endTime.year && startTime.month == endTime.month && startTime.day == endTime.day) {
                    String.format(
                        "%2d.%d.%d %d:%02d ~ %d:%02d",
                        startTime.year % 2000, startTime.month, startTime.day, startTime.hourOfDay, startTime.minute,
                        endTime.hourOfDay, endTime.minute
                    )
                } else {
                    String.format(
                        "%2d.%d.%d %d:%02d ~ %2d.%d.%d %d:%02d",
                        startTime.year % 2000, startTime.month, startTime.day, startTime.hourOfDay, startTime.minute,
                        endTime.year % 2000, endTime.month, endTime.day, endTime.hourOfDay, endTime.minute
                    )
                }
            }
            tvSummaryContent.text = "학습 내용 : ${curRound.detail}"
            tvSummaryPlace.text = String.format("장소 : %s", if(curRound.place != null) curRound.place else "온라인")
        }
    }
}