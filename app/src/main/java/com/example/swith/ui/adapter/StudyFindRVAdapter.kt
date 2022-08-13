package com.example.swith.ui.adapter

import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.swith.data.*
import com.example.swith.databinding.ItemLoadingBinding
import com.example.swith.databinding.ItemStudyFindBinding
import com.example.swith.ui.study.create.*
import java.lang.reflect.Array.set
import java.text.SimpleDateFormat
import java.util.*

class StudyFindRVAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        private const val TYPE_VIEW = 0
        private const val TYPE_LOADING = 1
    }

    private val studyList = mutableListOf<getStudyResponse?>()

    fun setData(studyList: ArrayList<getStudyResponse>)
    {
        this.studyList.apply{
            clear()
            addAll(studyList)
        }
        notifyDataSetChanged()
    }

    fun addData(studyList : ArrayList <getStudyResponse>)
    {
        Log.e("DATA",studyList.toString())
        this.studyList.addAll(studyList)
        notifyDataSetChanged()
    }

    fun setLoadingView(b:Boolean)
    {
        if (b)
        {
            android.os.Handler(Looper.getMainLooper()).post{
                this.studyList.add(null)
                notifyItemInserted(studyList.size - 1)
            }
        }
        else{
            if (this.studyList[studyList.size-1]==null){
                this.studyList.removeAt(studyList.size-1)
                notifyItemRemoved(studyList.size)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType)
        {
            TYPE_VIEW->{
                val inflatedView = LayoutInflater.from(parent.context)
                val binding = ItemStudyFindBinding.inflate(inflatedView,parent,false)
                return FindViewHolder(binding)
            }
            else ->{
                val inflatedView = LayoutInflater.from(parent.context)
                val binding = ItemLoadingBinding.inflate(inflatedView,parent,false)
                return LoadingViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position:Int):Int{
        return when(studyList[position]){
            null-> TYPE_LOADING
            else-> TYPE_VIEW
        }
    }

    override fun getItemCount():Int{
        return studyList.size
    }


    inner class FindViewHolder(val binding: ItemStudyFindBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(studyList:getStudyResponse?) {
            with(binding)
            {
                tvStudyTitle.text = studyList?.title
                tvSearchContent.text = studyList?.groupContent
                var formatter = SimpleDateFormat("yyyy-MM-dd")
                var date = formatter.parse("${studyList?.deadline}").time
                var today = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time.time
                tvSearchDeadline.text = "마감 D-${(date - today) / (60 * 60 * 24 * 1000)}"
                tvSearchPeople.text = studyList?.memberLimit.toString()
                tvSearchRegion.text = "인천광역시 남동구"
                tvSearchRegion.text = "인천광역시 남동구"
            }

            binding.root.setOnClickListener { v ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    if (itemClickListener != null) {
                        itemClickListener.onClick(v, pos)
                    }
                }
            }
        }
    }

    inner class LoadingViewHolder(val binding:ItemLoadingBinding):
        RecyclerView.ViewHolder(binding.root){
    }
    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(v:View, pos:Int)
    }
    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_VIEW -> {
                val findViewHolder = holder as FindViewHolder
                findViewHolder.onBind(studyList[position])
            }
        }
    }
}
