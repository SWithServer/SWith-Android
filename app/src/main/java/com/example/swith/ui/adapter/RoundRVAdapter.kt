package com.example.swith.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.data.R
import com.example.data.databinding.ItemRoundBinding
import com.example.data.entity.GetSessionRes
import com.example.data.entity.Round
import com.example.data.utils.compareTimeWithNow
import java.time.ZoneId
import java.time.ZonedDateTime

class RoundRVAdapter() : RecyclerView.Adapter<RoundRVAdapter.ViewHolder>() {
    private var roundList = ArrayList<GetSessionRes>()
    lateinit var binding: ItemRoundBinding

    interface myItemClickListener {
        fun onItemClick(round: GetSessionRes)
    }

    private lateinit var mItemClickListener: myItemClickListener

    fun setItemClickListener(itemClickListener: myItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_round,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(roundList[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(roundList[position]) }
    }

    override fun getItemCount(): Int = roundList.size

    fun setData(roundData: Round) {
        roundList = roundData.getSessionResList as ArrayList<GetSessionRes>
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: ItemRoundBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(round: GetSessionRes) {
            with(binding) {
                roundTitleTv.text = "${round.sessionNum}회차"
                roundDetailTv.text = round.sessionContent
                roundDateTv.text =
                    if (round.sessionStart[0] == ZonedDateTime.now(ZoneId.of("Asia/Seoul")).year)
                        String.format(
                            "%d월 %d일 %d:%02d",
                            round.sessionStart[1],
                            round.sessionStart[2],
                            round.sessionStart[3],
                            round.sessionStart[4]
                        )
                    else
                        String.format(
                            "%2d년 %d월 %d일 %d:%02d",
                            round.sessionStart[0] % 2000,
                            round.sessionStart[1],
                            round.sessionStart[2],
                            round.sessionStart[3],
                            round.sessionStart[4]
                        )
                if (!compareTimeWithNow(round.sessionEnd)) {
                    if (round.attendanceRate >= 0) roundAttendTv.text = "${round.attendanceRate} %"
                    else roundAttendTv.text = "0 %"
                    itemRoundAttendLayout.visibility = View.VISIBLE
                } else itemRoundAttendLayout.visibility = View.INVISIBLE
                roundPlaceTv.text = if (round.online == 0) "${round.place}" else "온라인"
            }
        }
    }
}