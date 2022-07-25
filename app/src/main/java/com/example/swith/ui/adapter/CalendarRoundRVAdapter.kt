package com.example.swith.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.data.Round
import com.example.swith.databinding.ItemCalendarRoundBinding

class CalendarRoundRVAdapter() : RecyclerView.Adapter<CalendarRoundRVAdapter.ViewHolder>(){
    private var roundList = ArrayList<Round>()
    lateinit var binding: ItemCalendarRoundBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemCalendarRoundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(roundList[position])
    }

    override fun getItemCount(): Int = roundList.size

    fun setData(roundData : ArrayList<Round>){
        roundList = roundData
    }

    fun addData(round: Round) {
        roundList.add(round)
    }

    inner class ViewHolder(val binding: ItemCalendarRoundBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(round: Round){
            with(binding){
                tvItemCalendarRound.text = "${round.count}회차"
                tvItemCalendarDetail.text = "내용 : ${round.detail}"
            }
        }
    }

}