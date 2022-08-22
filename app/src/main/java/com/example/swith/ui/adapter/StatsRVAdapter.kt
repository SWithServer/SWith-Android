package com.example.swith.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.data.GetUserAttendanceRes
import com.example.swith.databinding.ItemStatsBinding

class StatsRVAdapter : RecyclerView.Adapter<StatsRVAdapter.ViewHolder>() {
    private lateinit var binding: ItemStatsBinding
    private var attendList = ArrayList<GetUserAttendanceRes>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_stats, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(attendList[position])
    }

    override fun getItemCount(): Int = attendList.size

    fun setData(data: List<GetUserAttendanceRes>){
        attendList = data as ArrayList<GetUserAttendanceRes>
    }

    // true 일때 출석율 순 false 면 이름 순
    fun sortData(attendSort: Boolean){
        if (attendList.isNotEmpty()) {
            if (attendSort)
                attendList.sortWith(compareBy<GetUserAttendanceRes> { -it.attendanceRate}.thenBy { it.nickname })
            else attendList.sortBy { it.nickname }
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(val binding: ItemStatsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(attend: GetUserAttendanceRes){
            with(binding){
                tvItemStatsNickname.text = attend.nickname
                tvItemStatsRatio.text = "${attend.attendanceRate} %"
                progressItemStats.progress = attend.attendanceRate
            }
        }
    }
}