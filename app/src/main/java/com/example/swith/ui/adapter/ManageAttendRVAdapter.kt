package com.example.swith.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.data.GetAttendance
import com.example.swith.databinding.ItemManageAttendBinding

class ManageAttendRVAdapter : RecyclerView.Adapter<ManageAttendRVAdapter.ViewHolder>() {
    private lateinit var binding: ItemManageAttendBinding
    private var attendList = ArrayList<GetAttendance>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_manage_attend, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(attendList[position])
    }

    override fun getItemCount(): Int = attendList.size

    inner class ViewHolder(binding: ItemManageAttendBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(attendance: GetAttendance){

        }
    }
}