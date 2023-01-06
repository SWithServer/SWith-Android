package com.example.swith.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.databinding.ItemManageAttendBinding

class ManageAttendRVAdapter : RecyclerView.Adapter<ManageAttendRVAdapter.ViewHolder>() {
    private lateinit var binding: ItemManageAttendBinding
    private var attendList = ArrayList<com.example.swith.entity.GetAttendanceInfo>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_manage_attend,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(attendList[position], position)
    }

    override fun getItemCount(): Int = attendList.size

    fun setData(data: List<com.example.swith.entity.GetAttendanceInfo>) {
        attendList = data as ArrayList<com.example.swith.entity.GetAttendanceInfo>
        notifyDataSetChanged()
    }

    fun getData(): List<com.example.swith.entity.UpdateAttend> {
        val data = ArrayList<com.example.swith.entity.UpdateAttend>()
        for (i in 0 until itemCount) {
            data.add(
                com.example.swith.entity.UpdateAttend(
                    attendList[i].attendanceIdx,
                    attendList[i].status
                )
            )
        }
        return data
    }

    inner class ViewHolder(val binding: ItemManageAttendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(attendance: com.example.swith.entity.GetAttendanceInfo, adapterPos: Int) {
            with(binding) {
                tvItemManageAttendNickname.text = attendance.nickname
                if (attendance.status == 0) {
                    tvItemManageAttendTobe.visibility = View.VISIBLE
                    spinnerItemManageAttend.visibility = View.INVISIBLE
                } else {
                    tvItemManageAttendTobe.visibility = View.INVISIBLE
                    spinnerItemManageAttend.visibility = View.VISIBLE
                    val stringList = ArrayList<String>()
                    stringList.apply {
                        add("출석")
                        add("지각")
                        add("결석")
                    }
                    spinnerItemManageAttend.apply {
                        adapter = object : ArrayAdapter<String>(
                            context,
                            R.layout.item_manage_attend_status_spinner
                        ) {
                            override fun getView(
                                position: Int,
                                convertView: View?,
                                parent: ViewGroup
                            ): View {
                                val textView =
                                    super.getView(position, convertView, parent) as TextView
                                when (position) {
                                    0 -> textView.setTextColor(
                                        resources.getColor(
                                            R.color.color_1363DF,
                                            null
                                        )
                                    )
                                    1 -> textView.setTextColor(
                                        resources.getColor(
                                            R.color.color_ADA0FF,
                                            null
                                        )
                                    )
                                    2 -> textView.setTextColor(Color.RED)
                                }
                                return textView
                            }
                        }.apply { addAll(stringList) }
                        setSelection(attendance.status - 1)
                        // droplist를 spinner와 간격을 두고 나오도록
                        dropDownVerticalOffset = 90
                        // 스피너 선택시 변하는 로직
                        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                attendList[adapterPos].status = position + 1
                                when (position) {
                                    0 -> background.setTint(
                                        resources.getColor(
                                            R.color.color_1363DF,
                                            null
                                        )
                                    )
                                    1 -> background.setTint(
                                        resources.getColor(
                                            R.color.color_ADA0FF,
                                            null
                                        )
                                    )
                                    2 -> background.setTint(Color.RED)
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }
                        }
                    }
                }
            }
        }
    }
}