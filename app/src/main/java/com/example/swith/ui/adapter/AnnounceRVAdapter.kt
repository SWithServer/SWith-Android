package com.example.swith.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.databinding.ItemAnnounceBinding
import com.example.swith.utils.ItemTouchHelperListener

class AnnounceRVAdapter() : RecyclerView.Adapter<AnnounceRVAdapter.ViewHolder>(),
    ItemTouchHelperListener {
    private lateinit var binding: ItemAnnounceBinding
    private var announceList = ArrayList<com.example.swith.entity.Announce>()

    interface CustomListener {
        fun onDelete(announce: com.example.swith.entity.Announce)
        fun onItemClick(announce: com.example.swith.entity.Announce)
    }

    private lateinit var customListener: CustomListener

    fun setListener(listener: CustomListener) {
        customListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_announce,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(announceList[position])
        holder.itemView.setOnClickListener { customListener.onItemClick(announceList[position]) }
    }

    fun setData(announceData: List<com.example.swith.entity.Announce>) {
        announceList = announceData as ArrayList<com.example.swith.entity.Announce>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = announceList.size

    inner class ViewHolder(binding: ItemAnnounceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(announce: com.example.swith.entity.Announce) {
            with(binding) {
                tvAnnounceContent.text = announce.announcementContent
                tvAnnounceDate.text =
                    "${announce.createdAt[0]}/${announce.createdAt[1]}/${announce.createdAt[2]}"
            }
        }
    }

    override fun onDeleteButtonClick(position: Int) {
        customListener.onDelete(announceList[position])
    }
}