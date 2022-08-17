package com.example.swith.viewmodel

import androidx.lifecycle.*
import com.example.swith.data.Group
import com.example.swith.data.GroupList
import com.example.swith.repository.home.HomeRemoteDataSource
import com.example.swith.repository.home.HomeRepository
import com.example.swith.utils.SingleLiveEvent
import com.example.swith.utils.base.BaseViewModel
import com.example.swith.utils.error.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel() : BaseViewModel() {
    private val repository: HomeRepository = HomeRepository(HomeRemoteDataSource())
    private var _groupLiveData = SingleLiveEvent<GroupList>()

    val groupLiveData: LiveData<GroupList>
        get() = _groupLiveData

    fun loadData(){
        // val userId = SharedPrefManager().getLoginData()?.userIdx
        val userId = 1
        viewModelScope.launch{
            val res = repository.getAllStudy(this@HomeViewModel, userId)
            withContext(Dispatchers.Main) {
                if (res == null) mutableScreenState.postValue(ScreenState.RENDER)
                res?.let{
                    // region Idx 로 받는부분?! 추후 구현
//                    res.group.forEach {
//                        if (it.online != 0){
//                            withContext(Dispatchers.IO){
//
//                            }
//                        }
//                    }
                    _groupLiveData.value = res
                    mutableScreenState.postValue(ScreenState.RENDER)
                }
            }
        }
    }


    fun getEmptyOrNull() : Boolean{
        return if (_groupLiveData.value == null) true
        else _groupLiveData.value?.group.isNullOrEmpty()
    }

}