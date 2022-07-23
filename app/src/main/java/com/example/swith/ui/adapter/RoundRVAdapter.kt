package com.example.swith.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.data.Round
import com.example.swith.databinding.ItemRoundBinding
import java.time.LocalDateTime
import kotlin.math.round

class RoundRVAdapter(private val curCount: Int) : RecyclerView.Adapter<RoundRVAdapter.ViewHolder>() {
    private var roundList = ArrayList<Round>()
    lateinit var binding: ItemRoundBinding

    interface myItemClickListener{
        fun onItemClick(round: Round)
    }

    private lateinit var mItemClickListener: myItemClickListener

    fun setItemClickListener(itemClickListener : myItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemRoundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(roundList[position])
        holder.itemView.setOnClickListener{mItemClickListener.onItemClick(roundList[position])}
    }

    override fun getItemCount(): Int = roundList.size

    fun setData(roundData: ArrayList<Round>){
        roundList = roundData
        notifyDataSetChanged()
    }

    fun addRound(round: Round){
        roundList.add(round)
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: ItemRoundBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(round: Round){
            with(binding){
                roundTitleTv.text= round.count.toString() + "회차"
                roundDetailTv.text = round.detail
                roundDateTv.text = with(round.startTime){
                    if (year == LocalDateTime.now().year)
                        String.format("%d월 %d일 %d:%02d", month, day, hourOfDay, minute)
                    else
                        String.format("%2d년 %d월 %d일 %d:%02d", year % 2000, month, day, hourOfDay, minute)
                }
                roundAttendTv.text = if (round.attend > 0)"출석 : ${round.attend}명" else ""
            }
        }
    }
}