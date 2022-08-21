package com.example.swith.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.swith.data.ManageUserResult
import com.example.swith.databinding.ItemApplication1Binding

class ManageUserRVAdapter1 : RecyclerView.Adapter<ManageUserRVAdapter1.ViewHolder>(){

    private lateinit var binding:ItemApplication1Binding
    private var userList = mutableListOf<ManageUserResult>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageUserRVAdapter1.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
        binding = ItemApplication1Binding.inflate(inflatedView,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ManageUserRVAdapter1.ViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ViewHolder(val binding: ItemApplication1Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (userList: ManageUserResult?) {
            with(binding)
            {
                tvName.text = userList?.nickname
                Glide.with(itemView).load(userList?.profileImgUrl).into(ivProfile)
                btnResume.setOnClickListener { v ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        if (itemClickListener != null) {
                            var userIdx = userList!!.userIdx
                            itemClickListener.resumeClick(v, pos, userIdx)
                        }
                    }
                }
            }

            binding.clickUser.setOnClickListener { v ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    if (itemClickListener != null) {
                        var userIdx = userList!!.userIdx
                        itemClickListener.onClick(v,pos,userIdx)
                    }
                }
            }
        }
    }

    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(v:View, pos:Int,userIdx:Long?)
        fun resumeClick(v:View, pos:Int,userIdx:Long?)
    }

    fun setItemClickListener(itemClickListener:OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun setUser(userList : List<ManageUserResult>) {
        this.userList.apply {
            clear()
            addAll(userList)
        }
    }
}
