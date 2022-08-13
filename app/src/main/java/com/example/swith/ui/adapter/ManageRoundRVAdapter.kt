package com.example.swith.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.data.GetSessionRes
import com.example.swith.databinding.ItemMangeRoundBinding

class ManageRoundRVAdapter : RecyclerView.Adapter<ManageRoundRVAdapter.ViewHolder>() {
    private lateinit var binding: ItemMangeRoundBinding
    private var roundList = ArrayList<GetSessionRes>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_mange_round, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(roundList[position])
    }

    override fun getItemCount(): Int = roundList.size

    fun setData(data: List<GetSessionRes>){
        roundList = data as ArrayList<GetSessionRes>
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ItemMangeRoundBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(round: GetSessionRes){
            with(binding){
                tvItemManageRoundCount.text = "${round.sessionNum}회차"
                tvItemManageRoundDate.text = "날짜 : ${round.sessionStart[0]}/${round.sessionStart[1]}/${round.sessionStart[2]}"
            }
        }
    }

}