package com.example.swith.ui.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.data.GetAttendance
import com.example.swith.databinding.ItemAttendBinding

class AttendRVAdapter(private val userId: Long?) : RecyclerView.Adapter<AttendRVAdapter.ViewHolder>() {
    lateinit var binding: ItemAttendBinding
    private var userAttendList = ArrayList<GetAttendance>()

    override fun getItemCount(): Int = userAttendList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_attend, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userAttendList[position])
    }

    fun setData(userAttendData: List<GetAttendance>){
        userAttendList = userAttendData as ArrayList<GetAttendance>
        notifyDataSetChanged()
    }

    fun updateUserStatus(status: Int){
        for (i in userAttendList.indices){
            if (userId == userAttendList[i].userIdx) {
                userAttendList[i].status = status
                notifyItemChanged(i)
            }
        }
    }

    inner class ViewHolder(private val binding: ItemAttendBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(userAttend: GetAttendance){
            with(binding){
                if (userId == userAttend.userIdx)
                    tvItemName.typeface = Typeface.DEFAULT_BOLD
                tvItemName.text = userAttend.nickname
                tvItemAttend.apply {
                    when(userAttend.status){
                        0 -> {
                            text = "예정"
                        }
                        1 -> {
                            text = "출석"
                            setTextColor(resources.getColor(R.color.color_1363DF, null))
                        }
                        2 -> {
                            text = "지각"
                            setTextColor(resources.getColor(R.color.color_ADA0FF, null))
                        }
                        3 -> {
                            text = "결석"
                            setTextColor(Color.RED)
                        }
                    }
                }
            }
        }
    }
}