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
class ManageUserRVAdapter2 : RecyclerView.Adapter<ManageUserRVAdapter2.ViewHolder>(){

    private lateinit var binding: ItemApplication1Binding
    private var userList = mutableListOf<ManageUserResult>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageUserRVAdapter2.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
        binding = ItemApplication1Binding.inflate(inflatedView,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ManageUserRVAdapter2.ViewHolder, position: Int) {
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
                if (userList?.applicationContent==null)
                {
                    btnResume.visibility=View.GONE
                }
                else{
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
        fun onClick(v: View, pos:Int, userIdx:Long?)
        fun resumeClick(v: View, pos:Int, userIdx:Long?)
    }

//    interface CustomListener{
//        fun onDeleteClick(userList: ManageUserResult)
//    }

    fun setItemClickListener(itemClickListener:OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun setUser(userList : List<ManageUserResult>) {
        this.userList.apply {
            clear()
            addAll(userList)
        }
        notifyDataSetChanged()
    }

//    private lateinit var customListener: CustomListener
//
//    fun setCustomListener(listener: CustomListener){
//        customListener = listener
//    }
//
//    override fun onDeleteButtonClick(position: Int) {
//        customListener.onDeleteClick(userList[position])
//    }


}