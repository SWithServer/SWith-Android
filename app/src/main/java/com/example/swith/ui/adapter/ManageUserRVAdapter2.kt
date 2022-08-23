package com.example.swith.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.swith.data.GetSessionRes
import com.example.swith.data.ManageUserResult
import com.example.swith.databinding.ItemApplication1Binding
import com.example.swith.utils.ItemTouchHelperListener

// 유저관리 선착순 목록(지원서 불필요)
class ManageUserRVAdapter2 : RecyclerView.Adapter<ManageUserRVAdapter2.CustomViewHolder>(), ItemTouchHelperListener{

    var userList = mutableListOf<ManageUserResult>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):CustomViewHolder {
        val binding = ItemApplication1Binding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class CustomViewHolder(val binding : ItemApplication1Binding) : RecyclerView.ViewHolder(binding.root){
        fun bind (userInfo: ManageUserResult) {
            with(binding)
            {
                tvName.text = userInfo?.nickname
                Glide.with(itemView).load(userInfo?.profileImgUrl).into(ivProfile)
                if (userInfo?.applicationContent==null)
                {
                    btnResume.visibility=View.GONE
                }
                else{
                    btnResume.setOnClickListener { v ->
                        val pos = adapterPosition
                        if (pos != RecyclerView.NO_POSITION) {
                            if (itemClickListener != null) {
                                var applicationContent = userInfo!!.applicationContent
                                itemClickListener.resumeClick(v, pos, applicationContent)
                            }
                        }
                    }
                }
            }
            binding.clickUser.setOnClickListener { v ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    if (itemClickListener != null) {
                        var userIdx = userInfo!!.userIdx
                        itemClickListener.onClick(v,pos,userIdx)
                    }
                }
            }
        }
    }

    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(v: View, pos:Int, userIdx:Long?)
        fun resumeClick(v:View, pos:Int,applicationContent:String?)
    }

    fun setItemClickListener(itemClickListener:OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    interface CustomListener{
        fun onDeleteClick(userInfo: ManageUserResult,applicationIdx:Long)
    }
    private lateinit var customListener: CustomListener

    fun setCustomListener(listener: CustomListener){
        customListener = listener
    }

    override fun onDeleteButtonClick(position: Int) {
        customListener.onDeleteClick(userList[position],userList[position].applicationIdx)
    }


}