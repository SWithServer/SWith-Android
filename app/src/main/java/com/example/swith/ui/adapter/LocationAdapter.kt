package com.example.swith.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.databinding.ItemLocationBinding

class LocationAdapter(regionList: ArrayList<String>) :
    RecyclerView.Adapter<LocationAdapter.LocationHolder>() {
    var list = regionList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationHolder {
        val binding =
            ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationHolder, position: Int) {
        val region = list[position]
        holder.binding.tvCity.text = region
        //row 리스너
        holder.binding.root.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    //리스너 등록
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun getName(position: Int): String {
        return list[position]
    }

    class LocationHolder(val binding: ItemLocationBinding) : RecyclerView.ViewHolder(binding.root)

}

