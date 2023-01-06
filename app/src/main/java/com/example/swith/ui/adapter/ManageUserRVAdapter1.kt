package com.example.swith.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.swith.databinding.ItemApplication1Binding

class ManageUserRVAdapter1 : RecyclerView.Adapter<ManageUserRVAdapter1.CustomViewHolder>() {

    var userList = mutableListOf<com.example.swith.entity.ManageUserResult>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding =
            ItemApplication1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.e("create view holder", "true")
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class CustomViewHolder(val binding: ItemApplication1Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userInfo: com.example.swith.entity.ManageUserResult) {
            with(binding)
            {
                tvName.text = userInfo?.nickname
                Glide.with(itemView).load(userInfo?.profileImgUrl).into(ivProfile)
                btnResume.setOnClickListener { v ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        if (itemClickListener != null) {
                            var applicationContent = userInfo!!.applicationContent
                            var applicationIdx = userInfo!!.applicationIdx
                            Log.e("신청서 Idx", "${applicationIdx}")
                            itemClickListener.resumeClick(
                                v,
                                pos,
                                applicationContent,
                                applicationIdx
                            )
                        }
                    }
                }
            }
            binding.clickUser.setOnClickListener { v ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    if (itemClickListener != null) {
                        var userIdx = userInfo!!.userIdx
                        itemClickListener.onClick(v, pos, userIdx)
                    }
                }
            }
        }
    }

    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(v: View, pos: Int, userIdx: Long?)
        fun resumeClick(v: View, pos: Int, applicationContent: String?, applicationIdx: Long?)
    }

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}
