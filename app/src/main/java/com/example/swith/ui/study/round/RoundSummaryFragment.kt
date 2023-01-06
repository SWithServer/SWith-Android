package com.example.swith.ui.study.round


import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.swith.R
import com.example.swith.databinding.FragmentRoundSummaryBinding
import com.example.swith.entity.SessionInfo
import com.example.swith.utils.base.BaseFragment
import com.example.swith.viewmodel.RoundViewModel


class RoundSummaryFragment :
    BaseFragment<FragmentRoundSummaryBinding>(R.layout.fragment_round_summary) {
    private val viewModel: RoundViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.sessionLiveData.observe(viewLifecycleOwner, Observer {
            initView(it)
        })
    }

    private fun initView(session: SessionInfo) {
        with(binding) {
            tvSummaryCount.text = "${session.sessionNum}회차"
            val startTime = com.example.swith.entity.DateTime(
                session.sessionStart[0],
                session.sessionStart[1],
                session.sessionStart[2],
                session.sessionStart[3],
                session.sessionStart[4]
            )
            val endTime = com.example.swith.entity.DateTime(
                session.sessionEnd[0],
                session.sessionEnd[1],
                session.sessionEnd[2],
                session.sessionEnd[3],
                session.sessionEnd[4]
            )
            tvSummaryDate.text = with(session) {
                // 시작 날짜와 종료 날짜가 같은 경우 시작 시간의 날짜만 표시, 종료 시간의 날짜는 표시 X
                if (startTime.year == endTime.year && startTime.month == endTime.month && startTime.day == endTime.day) {
                    String.format(
                        "%2d.%d.%d %d:%02d ~ %d:%02d",
                        startTime.year % 2000,
                        startTime.month,
                        startTime.day,
                        startTime.hourOfDay,
                        startTime.minute,
                        endTime.hourOfDay,
                        endTime.minute
                    )
                } else {
                    String.format(
                        "%2d.%d.%d %d:%02d ~ %2d.%d.%d %d:%02d",
                        startTime.year % 2000,
                        startTime.month,
                        startTime.day,
                        startTime.hourOfDay,
                        startTime.minute,
                        endTime.year % 2000,
                        endTime.month,
                        endTime.day,
                        endTime.hourOfDay,
                        endTime.minute
                    )
                }
            }
            tvSummaryContent.text = "학습 내용 : ${session.sessionContent}"
            tvSummaryPlace.text = if (session.online == 0) session.place else "온라인"

            session.groupImgUrl?.let {
                if (!it.isNullOrEmpty()) Glide.with(requireContext()).load(it)
                    .error(R.color.color_d9d9d9).into(ivSummaryStudy)
            }
        }
    }
}