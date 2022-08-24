package com.example.swith.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.data.ResumeResult
import com.example.swith.databinding.ItemInterestingBinding
import com.example.swith.databinding.ItemResumeBinding

class ResumeAdapter() : RecyclerView.Adapter<ResumeAdapter.ResumeViewHolder>() {
    private var dataList: List<ResumeResult>? = null

    inner class ResumeViewHolder(val binding: ItemResumeBinding) : RecyclerView.ViewHolder(binding.root)

    fun setData(dataList: List<ResumeResult>) {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResumeViewHolder {
        val binding = ItemResumeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResumeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResumeViewHolder, position: Int) {
        val data = dataList?.get(position)
        val date = data?.createdAt
        val dataDetail = "${date?.get(0)}년 ${date?.get(1)}월 ${date?.get(2)}일"
        holder.binding.apply {
            resumeData = data
            tvCalender.text=dataDetail
            if (data?.status == 0) {
                tvStatusDetail.text = "승인대기"
            } else if (data?.status == 1) {
                tvStatusDetail.text = "승인"
            } else if (data?.status == 2) {
                tvStatusDetail.text = "거절"
            } else if (data?.status == 3) {
                tvStatusDetail.text = "추방"
            }

            //row 리스너
            root.setOnClickListener {
                itemClickListener.onClick(it, position)
            }
        }

    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    //리스너 등록
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

}