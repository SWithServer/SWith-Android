package com.example.swith.ui.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.data.UserAttend
import com.example.swith.databinding.ItemAttendBinding

class AttendRVAdapter(private val userId: Int) : RecyclerView.Adapter<AttendRVAdapter.ViewHolder>() {
    lateinit var binding: ItemAttendBinding
    private var userAttendList = ArrayList<UserAttend>()

    override fun getItemCount(): Int = userAttendList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_attend, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userAttendList[position])
    }

    fun setData(userAttendData: ArrayList<UserAttend>){
        userAttendList = userAttendData
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemAttendBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(userAttend: UserAttend){
            with(binding){
                if (userId == userAttend.userId)
                    tvItemName.typeface = Typeface.DEFAULT_BOLD
                tvItemName.text = userAttend.name
                tvItemAttend.apply {
                    when(userAttend.attend){
                        0 -> {
                            text = "예정"
                        }
                        1 -> {
                            text = "출석"
                            setTextColor(Color.GREEN)
                        }
                        2 -> {
                            text = "지각"
                            setTextColor(Color.MAGENTA)
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