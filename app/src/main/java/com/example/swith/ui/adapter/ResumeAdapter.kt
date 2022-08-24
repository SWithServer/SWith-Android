package com.example.swith.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.databinding.ItemInterestingBinding
import com.example.swith.databinding.ItemResumeBinding

class ResumeAdapter():RecyclerView.Adapter<ResumeAdapter.ResumeViewHolder>() {
    private var dataList: List<String>? = null

    inner class ResumeViewHolder(val binding: ItemResumeBinding):RecyclerView.ViewHolder(binding.root)

    fun setData(dataList:List<String>){
        this.dataList=dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResumeViewHolder {
        val binding = ItemResumeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ResumeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResumeViewHolder, position: Int) {
        holder.binding.tvTitle.text=dataList?.get(position)

        //row 리스너
        holder.binding.root.setOnClickListener {
            itemClickListener.onClick(it,position)
        }
    }

    override fun getItemCount(): Int {
        return dataList?.size?:0
    }

    //리스너 등록
    interface OnItemClickListener{
        fun onClick(v: View, position: Int)
    }
    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

}