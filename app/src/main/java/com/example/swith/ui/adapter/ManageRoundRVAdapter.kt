package com.example.swith.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.data.GetSessionRes
import com.example.swith.databinding.ItemMangeRoundBinding
import com.example.swith.utils.ItemTouchHelperListener
import com.example.swith.utils.compareTimeWithNow

class ManageRoundRVAdapter : RecyclerView.Adapter<ManageRoundRVAdapter.ViewHolder>(), ItemTouchHelperListener {
    private lateinit var binding: ItemMangeRoundBinding
    private var roundList = ArrayList<GetSessionRes>()

    interface CustomListener{
        fun onClick(round: GetSessionRes)
        fun onDeleteClick(round: GetSessionRes)
    }
    private lateinit var customListener: CustomListener

    fun setCustomListener(listener: CustomListener){
        customListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_mange_round, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(roundList[position])
        holder.itemView.setOnClickListener { customListener.onClick(roundList[position])}
    }

    override fun getItemCount(): Int = roundList.size

    fun setData(data: List<GetSessionRes>){
        roundList = data as ArrayList<GetSessionRes>
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemMangeRoundBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(round: GetSessionRes){
            with(binding){
                tvItemManageRoundCount.text = "${round.sessionNum}회차"
                tvItemManageRoundDate.text = "${round.sessionStart[0]}/${round.sessionStart[1]}/${round.sessionStart[2]}"
            }
        }
    }

    override fun onDeleteButtonClick(position: Int) {
        customListener.onDeleteClick(roundList[position])
    }

}