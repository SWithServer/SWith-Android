package com.example.swith.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.data.Group
import com.example.swith.data.GroupList
import com.example.swith.databinding.ItemStudyBinding
import java.util.*

class HomeStudyRVAdapter : RecyclerView.Adapter<HomeStudyRVAdapter.ViewHolder>() {
    private var groupList = ArrayList<Group>()
    private lateinit var binding: ItemStudyBinding

    interface myItemClickListener{
        fun onItemClick(group: Group)
    }

    private lateinit var mItemClickListener: myItemClickListener

    fun setMyItemClickListener(itemClickListener: myItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_study, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(groupList[position], position)
        holder.itemView.setOnClickListener{ mItemClickListener.onItemClick(groupList[position])}
    }

    override fun getItemCount(): Int = groupList.size

    fun setData(newList: GroupList){
        groupList = newList.group as ArrayList<Group>
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemStudyBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(group: Group, position: Int){
            with(binding) {
                studyTitleTv.text = group.title
                studyNoticeTv.text = group.announcementContent
                tvStudyAttendRate.text = "${group.attendanceRate}%"
                studyTotalPeople.text = "${group.memberLimit}명"
                tvItemStudyCategory.text = group.interestContent
                studyCountTv.text = "${group.sessionNum}회차 학습내용"
                studyContentTv.text = group.sessionContent
                if (position == itemCount - 1)
                    itemStudyLine.visibility = View.INVISIBLE
                if (group.online == 1) tvItemStudyPlace.text = "온라인"
                else{
                    if (group.regionIdx2.isNullOrEmpty()) tvItemStudyPlace.text = group.regionIdx1
                    else tvItemStudyPlace.text = "${group.regionIdx1} | ${group.regionIdx2}"
                }
            }
        }
    }
}