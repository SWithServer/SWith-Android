package com.example.swith.ui.study

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.LineBackgroundSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import com.example.swith.R
import com.example.swith.databinding.FragmentCalendarBinding
import com.example.swith.ui.BaseFragment
import com.example.swith.viewmodel.RoundViewModel
import com.prolificinteractive.materialcalendarview.*
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.*

class CalendarFragment : BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar){
    private val viewModel : RoundViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView(){
        with(LocalDateTime.now()){
            binding.tvTest.text = "${year}년 ${monthValue}월 ${dayOfMonth}일"
        }

        with(binding.calendarView){
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
                binding.tvTest.text = "${date.year}년 ${date.month}월 ${date.day}일"
            }

        }
    }

    inner class TodayDecorator: DayViewDecorator{
        override fun shouldDecorate(date: CalendarDay): Boolean {
            with(LocalDateTime.now()){
                if (year == date.year && monthValue == date.month && dayOfMonth == date.day) return true
            }
            return false
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object: ForegroundColorSpan(Color.rgb(255, 127, 0)) {})
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
        private val radius : Float = 8f
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
            canvas.drawCircle(((left + right) / 2 ).toFloat(), bottom + radius, radius, paint.apply { color = Color.BLUE })
            // 글짜 색만 다시 칠하기
            // 현재 진한 하늘색
            paint.color = Color.rgb(72, 180, 224)
        }
    }
}