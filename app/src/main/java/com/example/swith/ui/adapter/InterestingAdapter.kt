package com.example.swith.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.databinding.ItemInterestingBinding

class InterestingAdapter(private val interestingClickCallback: InterestingCallback) :
    RecyclerView.Adapter<InterestingAdapter.InterestingViewHolder>() {

    private var dataList: List<String>? = null
    private var interesting1: String = ""
    private var interesting2: String = ""

    fun setDataList(dataList: ArrayList<String>, interesting1: String, interesting2: String) {
        this.dataList = dataList
        this.interesting1 = interesting1
        this.interesting2 = interesting2
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestingViewHolder {
        val binding = DataBindingUtil.inflate<ItemInterestingBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_interesting,
            parent,
            false
        )
        return InterestingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InterestingViewHolder, position: Int) {
        dataList?.let {
            holder.binding.data = it[position]
            Log.e("doori", it[position])
            holder.binding.isSelect =
                (it[position] == interesting1) || (it[position] == interesting2)

            holder.binding.clInteresting.setOnClickListener {
                holder.binding.data?.let {
                    interestingClickCallback.onClick(it)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    class InterestingViewHolder(val binding: ItemInterestingBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface InterestingCallback {
        fun onClick(data: String)
    }
}


