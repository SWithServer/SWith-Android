package com.example.swith.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swith.domain.entity.Content
import com.example.swith.domain.entity.Result
import com.example.swith.domain.entity.StudyFindResponse
import com.example.swith.repository.study.StudyFindDataSource
import com.example.swith.repository.study.StudyFindRepository

class StudyFindViewModel : ViewModel() {
    private val studyFindRepository = StudyFindRepository(StudyFindDataSource())

    private var _contentLiveData = MutableLiveData<Result>()
    val contentLiveData:LiveData<Result>
        get() = _contentLiveData

    private var _loadMoreFlag = MutableLiveData<Boolean>().apply{
        value=false
    }

    val loadMoreFlag:LiveData<Boolean>
        get() = _loadMoreFlag

    fun loadData(title:String?,region:String?, interest1:Int?, interest2:Int?, sort:Int){
        studyFindRepository.loadData(title,region,interest1,interest2,sort).apply{
            if (this.value !=null){
                _contentLiveData.value = this.value
            }
            else{
                Log.e("summer", "null 값 들어옴")
            }
        }
    }

    fun loadMoreData(title:String?, region:String?,interest1: Int?,interest2: Int?,sort: Int,groupIdx:Long?){
        _loadMoreFlag.value=true
        studyFindRepository.loadMoreData(title,region,interest1,interest2,sort,groupIdx).apply{
            if (this.value!=null){
                _contentLiveData.value=this.value
            }
            else{
                Log.e("summer","null 값 들어옴")
            }
        }
    }

}