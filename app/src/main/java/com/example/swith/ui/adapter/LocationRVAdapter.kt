package com.example.swith.ui.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.data.Location
import com.example.swith.databinding.ItemLocationBinding

class LocationRVAdapter(var cityList:ArrayList<Location>):RecyclerView.Adapter<LocationRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding: ItemLocationBinding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cityList[position])
    }
    override fun getItemCount(): Int =cityList.size

    //뷰홀더 만들기
    inner class ViewHolder(val binding:ItemLocationBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(location:Location)
        {
            binding.tvItemCity.text=location.city
        }
    }
}