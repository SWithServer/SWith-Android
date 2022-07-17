package com.example.swith.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.data.Round
import com.example.swith.databinding.ItemRoundBinding

class RoundRVAdapter : RecyclerView.Adapter<RoundRVAdapter.ViewHolder>() {
    private var roundList = ArrayList<Round>()
    lateinit var binding: ItemRoundBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemRoundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(roundList[position])
    }

    override fun getItemCount(): Int = roundList.size

    fun setData(roundData: ArrayList<Round>){
        roundList = roundData
        notifyDataSetChanged()
    }

    fun addRound(round: Round){
        roundList.add(round)
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: ItemRoundBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(round: Round){
            with(binding){
                roundTitleTv.text= round.count.toString() + "회차"
                roundDetailTv.text = round.detail
                roundDateTv.text = round.startTime
            }
        }
    }
}