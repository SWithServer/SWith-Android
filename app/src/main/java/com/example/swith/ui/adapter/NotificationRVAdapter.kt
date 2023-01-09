package com.example.swith.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.databinding.ItemNotificationBinding
import com.example.swith.entity.Notification

class NotificationRVAdapter() : RecyclerView.Adapter<NotificationRVAdapter.ViewHolder>() {
    private lateinit var binding: ItemNotificationBinding
    private var notificationList = mutableListOf<Notification>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_notification, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notificationList[position])
    }
    override fun getItemCount(): Int= notificationList.size

    fun setData(data: List<Notification>){
        notificationList.clear()
        notificationList.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: Notification){
            binding.notification = notification
        }
    }


}