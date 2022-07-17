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
    // 임시 변수
    private var studyCount = 0

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
    init{
        if (studyCount == 0){
            // do nothing
        }
        else { studyData.apply {
            add(Study("영어 스터디", "회화", 8, 5))
            add(Study("자격증 뿌시자", "자격증", 7, 3))
        }}
        _studyLiveData.value = studyData
    }

    fun addStudy(study: Study){
        studyData.add(study)
        _studyLiveData.value = studyData
    }
}