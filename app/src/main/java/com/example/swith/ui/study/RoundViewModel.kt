package com.example.swith.ui.study

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swith.data.Round

class RoundViewModel : ViewModel() {
    private var roundData = ArrayList<Round>()
    private var _roundLiveData = MutableLiveData<ArrayList<Round>>()

    val roundLiveData : LiveData<ArrayList<Round>>
        get() = _roundLiveData

    fun initData(roundList: ArrayList<Round>){
        roundData = roundList
        _roundLiveData.value = roundData
    }

    fun addData(round: Round){
        roundData.add(round)
        _roundLiveData.value = roundData
    }
}