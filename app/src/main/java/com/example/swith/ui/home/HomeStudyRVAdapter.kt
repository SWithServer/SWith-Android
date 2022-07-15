package com.example.swith.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.data.Study
import com.example.swith.databinding.ItemStudyBinding

class HomeStudyRVAdapter : RecyclerView.Adapter<HomeStudyRVAdapter.ViewHolder>() {
    private var studyList = ArrayList<Study>()
    private lateinit var binding: ItemStudyBinding

    interface myItemClickListener{
        fun onItemClick(study: Study)
    }

    private lateinit var mItemClickListener: myItemClickListener

    fun setMyItemClickListener(itemClickListener: myItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeStudyRVAdapter.ViewHolder {
        binding = ItemStudyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeStudyRVAdapter.ViewHolder, position: Int) {
        holder.bind(studyList[position])
        holder.itemView.setOnClickListener{ mItemClickListener.onItemClick(studyList[position])}
    }

    override fun getItemCount(): Int = studyList.size

    fun setData(newList: ArrayList<Study>){
        studyList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemStudyBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(study: Study){
            with(binding) {
                studyTitleTv.text = study.title
                studyRoundTitle.text = study.totalRound.toString() + "회차"
                studyTotalPeople.text = study.totalPeople.toString() + "명"
                studyCategory.text = study.category
            }
        }
    }
}