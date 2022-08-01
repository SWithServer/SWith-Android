package com.example.swith.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.swith.data.DateTime
import com.example.swith.data.Group
import com.example.swith.data.GroupItem
import com.example.swith.repository.RetrofitApi
import com.example.swith.repository.RetrofitService
import com.example.swith.repository.home.HomeRemoteDataSource
import com.example.swith.repository.home.HomeRepository
import com.example.swith.utils.SharedPrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel() : ViewModel() {
    private val repository: HomeRepository = HomeRepository(HomeRemoteDataSource())
    private var groupData = ArrayList<GroupItem>()
    private var _groupLiveData = MutableLiveData<Group>()
    val groupLiveData: LiveData<Group>
        get() = _groupLiveData

    init{
        // val userId = SharedPrefManager().getLoginData()?.userIdx
        val userId = 1
        viewModelScope.launch{
            val res = repository.getAllStudy(userId)
            withContext(Dispatchers.Main) {
                res?.let { _groupLiveData.value = it }
                initTempData()
            }
        }
    }

    private fun initTempData(){
        val tempList = ArrayList<GroupItem>()
        tempList.add(GroupItem(1, "스터디 임시 1", 8, "컴퓨터", "임시 공지사항입니다.", 3, "스터디" ,
            listOf(2022, 7, 31, 16, 0), 80))
        _groupLiveData.value = Group(tempList)
        Log.e("value", groupLiveData.value.toString())
    }

}