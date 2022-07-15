package com.example.swith.ui.study

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swith.data.Round

class RoundViewModel : ViewModel() {
    private var allData = ArrayList<Round>()
    private var postData = ArrayList<Round>()

    private var pastVisible = false
    private var _roundLiveData = MutableLiveData<ArrayList<Round>>()

    val roundLiveData : LiveData<ArrayList<Round>>
        get() = _roundLiveData


    fun initData(roundList: ArrayList<Round>, cur: Int){
        roundList.forEach {
            if(it.count >= cur)
                postData.add(it)
            allData.add(it)
        }
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