package com.example.swith.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.data.GetSessionRes
import com.example.swith.data.Round
import com.example.swith.databinding.ItemRoundBinding
import java.time.LocalDateTime

class RoundRVAdapter() : RecyclerView.Adapter<RoundRVAdapter.ViewHolder>() {
    private var roundList = ArrayList<GetSessionRes>()
    lateinit var binding: ItemRoundBinding

    interface myItemClickListener{
        fun onItemClick(round: GetSessionRes)
    }

    private lateinit var mItemClickListener: myItemClickListener

    fun setItemClickListener(itemClickListener : myItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_round, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(roundList[position])
        holder.itemView.setOnClickListener{mItemClickListener.onItemClick(roundList[position])}
    }

    override fun getItemCount(): Int = roundList.size

    fun setData(roundData: Round){
        roundList = roundData.getSessionResList as ArrayList<GetSessionRes>
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: ItemRoundBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(round: GetSessionRes){
            with(binding){
                roundTitleTv.text= "${round.sessionNum}회차"
                roundDetailTv.text = round.sessionContent
                roundDateTv.text = if (round.sessionStart[0] == LocalDateTime.now().year)
                        String.format("%d월 %d일 %d:%02d", round.sessionStart[1], round.sessionStart[2], round.sessionStart[3], round.sessionStart[4])
                    else
                        String.format("%2d년 %d월 %d일 %d:%02d", round.sessionStart[0] % 2000, round.sessionStart[1], round.sessionStart[2], round.sessionStart[3], round.sessionStart[4])
                roundAttendTv.text = if(round.attendanceRate != null && round.attendanceRate >=0)  "${round.attendanceRate}%" else ""
                roundPlaceTv.text = if (round.online == 0) "장소 : ${round.place}" else "온라인"
            }
        }
    }
}