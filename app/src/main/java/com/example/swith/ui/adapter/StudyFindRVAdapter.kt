package com.example.swith.ui.adapter

import android.content.Context
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.R
import com.example.swith.data.*
import com.example.swith.databinding.FragmentStudyFindBinding
import com.example.swith.databinding.ItemLocationBinding
import com.example.swith.databinding.ItemStudyFindBinding
import com.example.swith.repository.ApiService
import com.example.swith.repository.RetrofitService
import com.example.swith.ui.profile.ProfileFragment
import com.example.swith.ui.study.create.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Array.set
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

//private val studyList:ArrayList<StudyFindData>

class StudyFindRVAdapter(val studyList : ArrayList<getStudyResponse>) : RecyclerView.Adapter<StudyFindRVAdapter.FindViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindViewHolder {
        val binding =
            ItemStudyFindBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FindViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FindViewHolder, position: Int) {
        holder.onBind((studyList[position]))
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(studyList[position])
        }
    }

    override fun getItemCount(): Int {
        return studyList.size
    }

    interface OnItemClickListener {
        fun onClick(stduyList : getStudyResponse)
    }


    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    private lateinit var itemClickListener: OnItemClickListener

    inner class FindViewHolder(val binding: ItemStudyFindBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(studyList: getStudyResponse) {
            with(binding)
            {
                tvStudyTitle.text = studyList.title
                tvStudyFindDetailContent.text = studyList.groupContent
                var formatter = SimpleDateFormat("yyyy-MM-dd")
                var date = formatter.parse("${studyList.deadline}").time
                var today = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time.time
                tvStudyDetailDeadline.text = "마감 D-${(date - today) / (60 * 60 * 24 * 1000)}"
                tvStudyDetailRecruitment.text = studyList.memberLimit.toString()
                tvStudyDetailRegion1.text = setRegion(studyList.regionIdx1!!.toLong())
                tvStudyDetailRegion2.text = setRegion(studyList.regionIdx2!!.toLong())
            }
        }
    }

    fun setRegion(code:Long) : String{
        var name_: String = ""
        val retrofit = Retrofit.Builder()
            .baseUrl(RetrofitService.REG_CODE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val regionService = retrofit.create(ApiService::class.java)
        regionService.getCityCode(code.toString()).enqueue(object : Callback<CityResponse> {
            override fun onResponse(
                call: Call<CityResponse>,
                response: Response<CityResponse>
            ) {
                response.body()?.apply {
                    val regResponse = this as CityResponse
                    val regcodes = regResponse.regcodes
                    for (a in regcodes) {
                        name_ = a.name
                    } }
            }
            override fun onFailure(call: Call<CityResponse>, t: Throwable) {
                Log.e("summer", t.toString())
                name_=""
            }
        })
        return name_
    }
}
