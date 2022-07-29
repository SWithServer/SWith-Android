package com.example.swith.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.data.Group
import com.example.swith.data.GroupItem
import com.example.swith.databinding.ItemStudyBinding
import java.util.*

class HomeStudyRVAdapter : RecyclerView.Adapter<HomeStudyRVAdapter.ViewHolder>() {
    private var groupList = ArrayList<GroupItem>()
    private lateinit var binding: ItemStudyBinding

    interface myItemClickListener{
        fun onItemClick(group: GroupItem)
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
        holder.bind(groupList[position])
        holder.itemView.setOnClickListener{ mItemClickListener.onItemClick(groupList[position])}
    }

    override fun getItemCount(): Int = groupList.size

    fun setData(newList: Group){
        groupList = newList.group as ArrayList<GroupItem>
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemStudyBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(group: GroupItem){
            with(binding) {
                studyTitleTv.text = group.title
                studyNoticeTv.text = group.announcementContent
                studyRoundTitle.text = group.sessionNum.toString() + "회차"
                studyAttendRate.text = "출석율 : ${group.attendanceRate}%"
                studyTotalPeople.text = "${group.memberLimit}명"
                studyCategory.text = group.interestContent
            }
        }
    }
}