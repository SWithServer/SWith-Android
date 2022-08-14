package com.example.swith.ui.study

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.forEach
import androidx.fragment.app.activityViewModels
import com.example.swith.R
import com.example.swith.databinding.FragmentStatsBinding
import com.example.swith.utils.base.BaseFragment
import com.example.swith.viewmodel.RoundViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet

class StatsFragment : BaseFragment<FragmentStatsBinding>(R.layout.fragment_stats) {
    private val viewModel : RoundViewModel by activityViewModels()
    private val sessionList by lazy{
        viewModel.getAllData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChart()
    }

    private fun initChart(){
        with(binding.chartAttend){
            description.isEnabled = false
            setTouchEnabled(false)
            setDrawBarShadow(false)
            setDrawValueAboveBar(false)
            legend.isEnabled = false
            setPinchZoom(false)
            setDrawGridBackground(false)

            // x축 레이아웃 설정
            val xAxis = xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.isEnabled = true
            xAxis.mAxisMinimum = 1f
            xAxis.mAxisMaximum = sessionList.size.toFloat()
            xAxis.textSize = 14f
            xAxis.setDrawAxisLine(true)
            xAxis.setDrawGridLines(false)
            xAxis.granularity = 1f
            xAxis.valueFormatter = object: ValueFormatter(){
                override fun getFormattedValue(value: Float): String {
                    return "${value.toInt()}회차"
                }
            }

            val yLeft = axisLeft
            yLeft.isEnabled = false

            val yRight = axisRight
            yRight.setDrawAxisLine(true)
            yRight.setDrawGridLines(true)
            yRight.setDrawLabels(true)
            yRight.textSize = 14f
            yRight.isEnabled = true

            setFitBars(true)

            // 데이터 init
            val entries = ArrayList<BarEntry>()
            for (i in sessionList.indices){
                entries.add(BarEntry(sessionList[i].sessionNum.toFloat(), sessionList[i].attendanceRate.toFloat()))
            }

            val barDataSet = BarDataSet(entries, "출석율")
            barDataSet.colors = listOf(Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.YELLOW, Color.GRAY)
            barDataSet.formLineWidth = 2.5f
            barDataSet.setDrawIcons(false)
            barDataSet.setDrawValues(true)
            barDataSet.valueFormatter = object : ValueFormatter(){
                override fun getFormattedValue(value: Float): String {
                    return "${value.toInt()}%"
                }
            }

            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(barDataSet)

            var barData = BarData(dataSets)
            barData.setValueTextSize(12f)
            barData.barWidth = 0.8f

            data = barData
            invalidate()
        }
    }
}