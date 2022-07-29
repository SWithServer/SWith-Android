package com.example.swith.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swith.data.DateTime
import com.example.swith.data.Group
import com.example.swith.data.GroupItem
import com.example.swith.repository.RetrofitApi
import com.example.swith.repository.RetrofitService
import com.example.swith.repository.home.HomeRemoteDataSource
import com.example.swith.repository.home.HomeRepository
import com.example.swith.utils.SharedPrefManager
import kotlinx.coroutines.Dispatchers
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
            val data = repository.getAllStudy(userId)
            withContext(Dispatchers.Main){
                _groupLiveData.value = data.value
            }
        }
    }


}