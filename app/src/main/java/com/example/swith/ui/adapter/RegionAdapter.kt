package com.example.swith.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.databinding.ItemRegionBinding
//
//class RegionAdapter(regionList: ArrayList<String>): RecyclerView.Adapter<RegionHolder>() {
//        var list = regionList
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionHolder {
//        val binding = ItemRegionBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//        return RegionHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: RegionHolder, position: Int) {
//        val region = list[position]
//        holder.binding.tvName.text=region
//
//        //row 리스너
//        holder.binding.root.setOnClickListener {
//            itemClickListener.onClick(it,position)
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    //리스너 등록
//    interface OnItemClickListener{
//        fun onClick(v: View, position: Int)
//    }
//    private lateinit var itemClickListener : OnItemClickListener
//
//    fun setItemClickListener(itemClickListener: OnItemClickListener) {
//        this.itemClickListener = itemClickListener
//    }
//
//    fun getName(position: Int): String {
//        return list[position]
//    }
//}
//class  RegionHolder(val binding: ItemRegionBinding): RecyclerView.ViewHolder(binding.root)