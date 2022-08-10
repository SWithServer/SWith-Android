package com.example.swith.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.data.Announce
import com.example.swith.databinding.ItemAnnounceBinding

class AnnounceRVAdapter(private val isManager: Boolean) : RecyclerView.Adapter<AnnounceRVAdapter.ViewHolder>(){
    private lateinit var binding: ItemAnnounceBinding
    private var announceList = ArrayList<Announce>()

    interface CustomListener{
        fun onDelete(announce: Announce)
        fun onItemClick(announce: Announce)
    }

    private lateinit var customListener: CustomListener

    fun setListener(listener: CustomListener){
        customListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_announce, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(announceList[position])
        holder.itemView.setOnClickListener { customListener.onItemClick(announceList[position]) }
    }

    fun setData(announceData: List<Announce>){
        announceList = announceData as ArrayList<Announce>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = announceList.size

    inner class ViewHolder(binding: ItemAnnounceBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(announce: Announce){
            with(binding){
                ibAnnounceDelete.apply {
                    visibility = if(isManager) View.VISIBLE else View.INVISIBLE
                    setOnClickListener { customListener.onDelete(announce) }
                }
                tvAnnounceContent.text = announce.announcementContent
                tvAnnounceDate.text = "날짜 : ${announce.createdAt[0]}/${announce.createdAt[1]}/${announce.createdAt[2]}"


            }
        }
    }
}