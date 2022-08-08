package com.example.swith.ui.adapter

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.data.StudyFindData
import com.example.swith.databinding.FragmentStudyFindBinding
import com.example.swith.databinding.ItemLocationBinding
import com.example.swith.databinding.ItemStudyFindBinding
import java.lang.reflect.Array.set
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class StudyFindRVAdapter(private val studyList:ArrayList<StudyFindData>) : RecyclerView.Adapter<FindViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindViewHolder {
        val binding = ItemStudyFindBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FindViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FindViewHolder, position: Int) {
      holder.onBind((studyList[position]))
    }

    override fun getItemCount(): Int {
       return studyList.size
    }
}

class FindViewHolder (val binding: ItemStudyFindBinding): RecyclerView.ViewHolder(binding.root)
{
    val title = binding.tvStudyTitle
    val content = binding.tvStudyFindDetailContent
    val deadline = binding.tvStudyDetailDeadline
    val people= binding.tvStudyDetailRecruitment
    val region = binding.tvStudyDetailRegion
    fun onBind (studyList : StudyFindData)
    {
        title.text= studyList.title
        content.text=studyList.content
        var formatter = SimpleDateFormat("yyyy-MM-dd")
        var date = formatter.parse("${studyList.deadLine}").time
        var today = Calendar.getInstance().apply{
            set(Calendar.HOUR_OF_DAY,0)
            set(Calendar.MINUTE,0)
            set(Calendar.SECOND,0)
            set(Calendar.MILLISECOND,0)
        }.time.time
        deadline.text="마감 D-${(date-today)/(60 * 60 * 24 * 1000)}"
        people.text=studyList.people.toString()
        region.text = studyList.region
    }
}