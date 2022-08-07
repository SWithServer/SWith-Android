package com.example.swith.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swith.data.Announce
import com.example.swith.data.AnnounceList
import com.example.swith.utils.base.BaseViewModel

class AnnounceViewModel : BaseViewModel() {
    private var _announceLiveData = MutableLiveData<AnnounceList>()

    val announceLiveData : LiveData<AnnounceList>
        get() = _announceLiveData

    fun loadData(){
        tempData()
    }

    private fun tempData(){
        val tempList = ArrayList<Announce>()
        tempList.add(Announce("1회차 발표 예정입니다!", 1, listOf(2022, 6, 3)))
        tempList.add(Announce("2회차 발표 예정입니다!", 2, listOf(2022, 6, 5)))
        tempList.add(Announce("3회차 발표 예정입니다!", 3, listOf(2022, 6, 7)))
        _announceLiveData.value = AnnounceList(tempList)
    }
}