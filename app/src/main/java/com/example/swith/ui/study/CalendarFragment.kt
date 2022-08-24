package com.example.swith.ui.study

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import androidx.lifecycle.Observer
import android.os.Bundle
import android.text.style.LineBackgroundSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swith.R
import com.example.swith.databinding.FragmentCalendarBinding
import com.example.swith.utils.base.BaseFragment
import com.example.swith.ui.adapter.CalendarRoundRVAdapter
import com.example.swith.ui.study.create.RoundCreateActivity
import com.example.swith.utils.compareDayWithNow
import com.example.swith.viewmodel.RoundViewModel
import com.prolificinteractive.materialcalendarview.*
import java.time.ZoneId
import java.time.ZonedDateTime

class CalendarFragment : BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar){
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private val viewModel : RoundViewModel by activityViewModels()
    private val nowTimezone by lazy {
        ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        observeViewModel()

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            if(it.resultCode == Activity.RESULT_OK){
                viewModel.loadData()
                binding.calendarView.selectedDate?.let{
                    binding.calendarView.removeDecorators()
                    binding.calendarView.addDecorators(TodayDecorator(), RoundDecorator())
                }
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView(){
        with(binding.rvCalendarRound){
            adapter = CalendarRoundRVAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        with(binding.calendarView) {
            state().edit()
                .isCacheCalendarPositionEnabled(false)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()
            addDecorators(RoundDecorator(), TodayDecorator())
            setHeaderTextAppearance(R.style.CustomHeader)
            setWeekDayTextAppearance(R.style.CustomWeekDay)
            setDateTextAppearance(R.style.CustomDate)
            setDateSelected(CalendarDay.today(), true)

            setOnDateChangedListener { _, date, _ ->
                binding.tvCalendarDate.text = "${date.year % 1000}/${date.month}/${date.day}"
                viewModel.setCalendarData(date.year, date.month, date.day)
            }

        }

        binding.btnCreateCalendar.setOnClickListener { startRoundCreateActivity() }
        binding.btnNoCreateCalendar.setOnClickListener { startRoundCreateActivity() }
    }

    private fun startRoundCreateActivity(){
        activityResultLauncher.launch(Intent(activity, RoundCreateActivity::class.java).apply {
            putExtra("minuteMin", viewModel.roundLiveData.value?.attendanceValidTime)
            putExtra("groupIdx", viewModel.groupIdx)
        })
    }

    private fun observeViewModel(){
        with(nowTimezone){
            binding.tvCalendarDate.text = "${year%1000}/${monthValue}/${dayOfMonth}"
            viewModel.setCalendarData(year, monthValue, dayOfMonth)
        }

        with(viewModel.calendarLiveData){
            observe(viewLifecycleOwner, Observer {
                (binding.rvCalendarRound.adapter as CalendarRoundRVAdapter).setData(it)
                value?.isEmpty()?.let { e -> setRVVisibility(e)}
            })
        }

        viewModel.roundLiveData.observe(viewLifecycleOwner, Observer {
            binding.calendarView.selectedDate?.let { c ->
                viewModel.setCalendarData(c.year, c.month, c.day)
                initView()
            }
        })

    }

    inner class TodayDecorator: DayViewDecorator{
        override fun shouldDecorate(date: CalendarDay): Boolean {
            with(nowTimezone){
                if (year == date.year && monthValue == date.month && dayOfMonth == date.day) return true
            }
            return false
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object: StyleSpan(Typeface.BOLD){} )
        }
    }

    inner class RoundDecorator: DayViewDecorator{
        override fun shouldDecorate(date: CalendarDay): Boolean = viewModel.roundDayExists(date.year, date.month, date.day)

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(CustomDotSpan())
        }
    }

    inner class CustomDotSpan : LineBackgroundSpan{
        private val radius : Float = 10f
        override fun drawBackground(
            canvas: Canvas,
            paint: Paint,
            left: Int,
            right: Int,
            top: Int,
            baseline: Int,
            bottom: Int,
            text: CharSequence,
            start: Int,
            end: Int,
            lineNumber: Int
        ) {
            // 원 그리고 색깔 칠하기
            canvas.drawCircle(((left + right) / 2 ).toFloat(), bottom + radius, radius, paint.apply { color = resources.getColor(R.color.color_1363DF, null) })
            // 글짜 색만 다시 칠하기
            paint.color = resources.getColor(R.color.color_003C99, null)
        }
    }

    private fun setRVVisibility(isEmpty: Boolean){
        with(binding){
            rvCalendarRound.visibility = if (isEmpty) View.INVISIBLE else View.VISIBLE
            if (!compareDayWithNow(calendarView.selectedDate!!)){
                tvNoRound.visibility = if (isEmpty) View.VISIBLE else View.INVISIBLE
                tvNoRound.setText(R.string.calendar_no_round_before)
                btnNoCreateCalendar.visibility = View.INVISIBLE
                btnCreateCalendar.visibility = View.INVISIBLE
            } else {
                // manager 인 경우
                if (viewModel.roundLiveData.value?.admin == true){
                    btnCreateCalendar.visibility = if(isEmpty) View.INVISIBLE else View.VISIBLE
                    tvNoRound.setText(R.string.calendar_no_round_manage)
                    tvNoRound.visibility = if (isEmpty) View.VISIBLE else View.INVISIBLE
                    btnNoCreateCalendar.visibility = if (isEmpty) View.VISIBLE else View.INVISIBLE
                } else {
                    btnCreateCalendar.visibility = View.INVISIBLE
                    tvNoRound.setText(R.string.calendar_no_round_default)
                    tvNoRound.visibility = if (isEmpty) View.VISIBLE else View.INVISIBLE
                    btnNoCreateCalendar.visibility = View.INVISIBLE
                }
            }
        }
    }
}