package com.example.swith.ui.study

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swith.data.Round

class RoundViewModel : ViewModel() {
    // 현재 회차 임시 변수
    private val curCount = 3

    private var allData = ArrayList<Round>()
    private var postData = ArrayList<Round>()

    private var pastVisible = false
    private var _roundLiveData = MutableLiveData<ArrayList<Round>>()

    val roundLiveData : LiveData<ArrayList<Round>>
        get() = _roundLiveData


    init{
        // 임시로 여기다 추가
        // 후에 repository로 받아오는 부분
        val roundData = ArrayList<Round>()
        roundData.add(Round(1, "22/7/12", "22/7/16", "영어 1회차 스터디", true, null))
        roundData.add(Round(2, "22/7/13", "22/7/16", "영어 2회차 스터디", true, null))
        roundData.add(Round(3, "22/7/14", "22/7/16", "영어 3회차 스터디", true, null))
        roundData.add(Round(4, "22/7/14", "22/7/16", "영어 4회차 스터디", true, null))
        roundData.add(Round(5, "22/7/14", "22/7/16", "영어 5회차 스터디", true, null))
        roundData.add(Round(6, "22/7/14", "22/7/16", "영어 6회차 스터디", true, null))

        allData.addAll(roundData)
        roundData.forEach { if (it.count >= curCount) postData.add(it)  }
        _roundLiveData.value = postData
    }

    fun addData(round: Round){
        postData.add(round)
        allData.add(round)

        _roundLiveData.value = if (pastVisible) allData else postData
    }

    fun setPastData(pastVisible: Boolean){
        this.pastVisible = pastVisible
        _roundLiveData.value = if (pastVisible) allData else postData
    }

}