package com.example.swith.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.swith.data.Announce
import com.example.swith.data.AnnounceList
import com.example.swith.repository.announce.AnnounceRemoteDataSource
import com.example.swith.repository.announce.AnnounceRepository
import com.example.swith.utils.SingleLiveEvent
import com.example.swith.utils.base.BaseViewModel
import com.example.swith.utils.error.ScreenState
import kotlinx.coroutines.launch

class AnnounceViewModel : BaseViewModel() {
    private val repository = AnnounceRepository(AnnounceRemoteDataSource())
    private var _announceLiveData = SingleLiveEvent<AnnounceList>()

    val announceLiveData : LiveData<AnnounceList>
        get() = _announceLiveData

    fun loadData(groupIdx: Int){
        viewModelScope.launch {
            val res = repository.getAllAnnounce(this@AnnounceViewModel, groupIdx)
            if (res == null) mutableScreenState.postValue(ScreenState.RENDER) else{
                mutableScreenState.postValue(ScreenState.RENDER)
                _announceLiveData.value = res!!
            }
        }
    }

//    private fun tempData(){
//
//        val tempList = ArrayList<Announce>()
//        tempList.add(Announce("1회차 발표 예정입니다!", 1, listOf(2022, 6, 3)))
//        tempList.add(Announce("2회차 발표 예정입니다!", 2, listOf(2022, 6, 5)))
//        tempList.add(Announce("3회차 발표 예정입니다!", 3, listOf(2022, 6, 7)))
//        _announceLiveData.value = AnnounceList(tempList)
//    }
}