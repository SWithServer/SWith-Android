package com.example.swith.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.data.RatingResult
import com.example.swith.databinding.ItemRatingBinding


class RatingAdapter():RecyclerView.Adapter<RatingAdapter.RatingViewHolder>() {
    lateinit var binding:ItemRatingBinding
    inner class RatingViewHolder(val binding: ItemRatingBinding) : RecyclerView.ViewHolder(binding.root)
    var dataList:List<RatingResult>?=null


    fun setData(dataList: List<RatingResult>) {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        binding = ItemRatingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        binding.tvName.text=dataList?.get(position)?.nickname
        //row 리스너
//        binding.root.setOnClickListener {
//            itemClickListener.onClick(it, position,binding.ratingbar.rating)
//        }
        binding.ratingbar.setOnClickListener{
            Log.e("doori",binding.ratingbar.rating.toString())
        }
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }


    //리스너 등록
    interface OnItemClickListener {
        fun onClick(v: View, position: Int,rating:Float)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

}