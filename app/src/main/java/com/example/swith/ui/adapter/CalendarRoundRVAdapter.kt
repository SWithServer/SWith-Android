package com.example.swith.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.data.GetSessionRes
import com.example.swith.databinding.ItemCalendarRoundBinding

class CalendarRoundRVAdapter() : RecyclerView.Adapter<CalendarRoundRVAdapter.ViewHolder>(){
    private var roundList = ArrayList<GetSessionRes>()
    lateinit var binding: ItemCalendarRoundBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_calendar_round, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(roundList[position])
    }

    override fun getItemCount(): Int = roundList.size

    fun setData(roundData : ArrayList<GetSessionRes>){
        roundList = roundData
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: ItemCalendarRoundBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(round: GetSessionRes){
            with(binding){
                tvItemCalendarRound.text = "${round.sessionNum}회차"
                tvItemCalendarDetail.text = "${round.sessionContent}"
            }
        }
    }

}