package com.example.swith.ui.study

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.forEach
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swith.R
import com.example.swith.data.GetUserAttendanceRes
import com.example.swith.databinding.FragmentStatsBinding
import com.example.swith.ui.adapter.ManageAttendRVAdapter
import com.example.swith.ui.adapter.StatsRVAdapter
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeViewModel()
    }

    private fun initView(){
        binding.rvStats.apply {
            adapter = StatsRVAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun initSpinner(){
        val stringList = ArrayList<String>()
        stringList.apply {
            add("출석율 순")
            add("이름순")
        }
        binding.spinnerStatsSort.apply {
            adapter = object: ArrayAdapter<String>(requireContext(), R.layout.item_manage_attend_spinner){
            }.apply { addAll(stringList) }
            dropDownVerticalOffset = dipToPixels(31f).toInt()
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    (binding.rvStats.adapter as StatsRVAdapter).sortData(position == 0)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
        }
    }

    private fun observeViewModel(){
        setVisibility(true, true)
        viewModel.loadUserAttend()

        viewModel.userAttendLiveData.observe(viewLifecycleOwner, Observer {
//            initChart(it.getUserAttendanceResList)
            (binding.rvStats.adapter as StatsRVAdapter).setData(it.getUserAttendanceResList)
            initSpinner()
        })

        viewModel.mutableScreenState.observe(viewLifecycleOwner, Observer {
            setVisibility(false, viewModel.userAttendLiveData.value?.getUserAttendanceResList.isNullOrEmpty())
        })
    }


//    private fun initChart(attendList: List<GetUserAttendanceRes>){
//        with(binding.chartAttend){
//            description.isEnabled = false
//            setTouchEnabled(false)
//            setDrawBarShadow(false)
//            setDrawValueAboveBar(false)
//            legend.isEnabled = false
//            setPinchZoom(false)
//            setDrawGridBackground(false)
//
//            // x축 레이아웃 설정
//            val xAxis = xAxis
//            xAxis.position = XAxis.XAxisPosition.BOTTOM
//            xAxis.isEnabled = true
//            xAxis.mAxisMinimum = 0f
//            xAxis.mAxisMaximum = attendList.size.toFloat()
//            xAxis.textSize = 14f
//            xAxis.setDrawAxisLine(true)
//            xAxis.setDrawGridLines(false)
//            xAxis.granularity = 1f
//            xAxis.valueFormatter = object: ValueFormatter(){
//                override fun getFormattedValue(value: Float): String {
//                    return attendList[value.toInt()].nickname
//                }
//            }
//
//            val yLeft = axisLeft
//            yLeft.isEnabled = false
//
//            val yRight = axisRight
//            yRight.setDrawAxisLine(true)
//            yRight.setDrawGridLines(true)
//            yRight.setDrawLabels(true)
//            yRight.textSize = 14f
//            yRight.isEnabled = true
//
//            setFitBars(true)
//
//            // 데이터 init
//            val entries = ArrayList<BarEntry>()
//            for (i in attendList.indices){
//                entries.add(BarEntry(i.toFloat(), attendList[i].attendanceRate.toFloat()))
//            }
//
//            val barDataSet = BarDataSet(entries, "출석율")
//            barDataSet.colors = listOf(Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.YELLOW, Color.GRAY)
//            barDataSet.formLineWidth = 2.5f
//            barDataSet.setDrawIcons(false)
//            barDataSet.setDrawValues(true)
//            barDataSet.valueFormatter = object : ValueFormatter(){
//                override fun getFormattedValue(value: Float): String {
//                    return "${value.toInt()}%"
//                }
//            }
//
//            val dataSets = ArrayList<IBarDataSet>()
//            dataSets.add(barDataSet)
//
//            var barData = BarData(dataSets)
//            barData.setValueTextSize(12f)
//            barData.barWidth = 0.8f
//
//            data = barData
//            invalidate()
//        }
//    }

    private fun setVisibility(beforeLoad: Boolean, isNull : Boolean){
        with(binding){
            if (beforeLoad){
                statsLayout.visibility = View.INVISIBLE
                statsCircularLayout.visibility = View.VISIBLE
            } else {
                statsLayout.visibility = View.VISIBLE
                statsCircularLayout.visibility = View.INVISIBLE
                rvStats.visibility = if (isNull) View.INVISIBLE else View.VISIBLE
                tvNoPeopleStats.visibility = if (isNull) View.VISIBLE else View.INVISIBLE
                spinnerStatsSort.visibility = if (isNull) View.INVISIBLE else View.VISIBLE
            }
        }
    }

    private fun dipToPixels(dipValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dipValue,
            resources.displayMetrics
        )
    }
}