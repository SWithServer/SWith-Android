package com.example.swith.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swith.data.DateTime
import com.example.swith.data.Round

class RoundViewModel() : ViewModel() {
    // 현재 회차 임시 변수

    private var allData = ArrayList<Round>()
    private var postData = ArrayList<Round>()
    val curCount = 3

    private var pastVisible = false
    private var _currentLiveData = MutableLiveData<Round>()
    private var _roundLiveData = MutableLiveData<ArrayList<Round>>()

    val currentLiveData : LiveData<Round>
        get() = _currentLiveData

    val roundLiveData : LiveData<ArrayList<Round>>
        get() = _roundLiveData

    init{
        // 임시로 여기다 추가
        // 후에 repository로 받아오는 부분
        val roundData = ArrayList<Round>()
        roundData.add(Round(1, DateTime(2022, 7, 12, 10, 0), DateTime(2022, 7, 12, 11, 0), "영어 1회차 스터디", true, null, 1))
        roundData.add(Round(2, DateTime(2022, 7, 13, 11, 0), DateTime(2022, 7, 12, 12, 0), "영어 2회차 스터디", true, null, 3))
        roundData.add(Round(3, DateTime(2022, 7, 14, 10, 0), DateTime(2022, 7, 14, 11, 0), "영어 3회차 스터디", true, null, 5))
        roundData.add(Round(4, DateTime(2022, 7, 15, 22, 0), DateTime(2022, 7, 15, 23, 0), "영어 4회차 스터디", true, null, 0))
        roundData.add(Round(5, DateTime(2022, 7, 15, 23, 30), DateTime(2022, 7, 16, 0, 0), "영어 5회차 스터디", true, null, 0))
        roundData.add(Round(6, DateTime(2022, 7, 16, 22, 0), DateTime(2022, 7, 16, 23, 0), "영어 6회차 스터디", true, null, 0 ))

        allData.addAll(roundData)
        roundData.forEach { if (it.count >= curCount) postData.add(it)  }
        _roundLiveData.value = postData
    }

    fun addData(round: Round){
        postData.add(round)
        allData.add(round)

        _roundLiveData.value = if (pastVisible) allData else postData
    }

    fun setCurrentData(round: Round){
        _currentLiveData.value = round
    }

    fun setPastData(pastVisible: Boolean){
        this.pastVisible = pastVisible
        _roundLiveData.value = if (pastVisible) allData else postData
    }

}