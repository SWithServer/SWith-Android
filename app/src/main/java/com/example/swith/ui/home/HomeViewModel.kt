package com.example.swith.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swith.data.Study
import com.example.swith.repository.home.HomeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel() : ViewModel() {
    private var studyData = ArrayList<Study>()
    private var _studyLiveData = MutableLiveData<ArrayList<Study>>()
    val studyLiveData: LiveData<ArrayList<Study>>
        get() = _studyLiveData

//    init{
//        viewModelScope.launch {
//            // 수정 필요
//            val response = HomeRepository().getAllStudy()
//            if (response.isSuccessful){
//
//            }
//        }
//    }
    fun initData(studyList: ArrayList<Study>){
        studyData.addAll(studyList)
        _studyLiveData.value = studyData

    }

    fun addStudy(study: Study){
        studyData.add(study)
        _studyLiveData.value = studyData
    }

}