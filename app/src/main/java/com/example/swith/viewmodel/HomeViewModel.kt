package com.example.swith.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.swith.data.Group
import com.example.swith.data.GroupList
import com.example.swith.repository.home.HomeRemoteDataSource
import com.example.swith.repository.home.HomeRepository
import com.example.swith.utils.SharedPrefManager
import com.example.swith.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel() : ViewModel() {
    private val repository: HomeRepository = HomeRepository(HomeRemoteDataSource())
    private var _groupLiveData = SingleLiveEvent<GroupList>()

    val groupLiveData: LiveData<GroupList>
        get() = _groupLiveData

    fun loadData(){
        // val userId = SharedPrefManager().getLoginData()?.userIdx
        val userId = 1
        viewModelScope.launch{
            val res = repository.getAllStudy(userId)
            withContext(Dispatchers.Main) {
                res?.let { _groupLiveData.value = it }
                if (res == null) _groupLiveData.call()
            }
        }
    }


    fun getEmptyOrNull() : Boolean{
        return if (_groupLiveData.value == null) true
        else _groupLiveData.value?.group.isNullOrEmpty()
    }

    private fun initTempData(){
        val tempList = ArrayList<Group>()
        tempList.add(Group("임시 공지사항입니다.",80, 1, "컴퓨터",8, "임시 임시", 3,
            listOf(2022, 7, 31, 16, 0), "스터디 임시 1"))
        _groupLiveData.value = GroupList(tempList)
    }

}