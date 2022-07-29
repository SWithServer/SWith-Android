package com.example.swith.repository.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swith.data.Group
import com.example.swith.repository.RetrofitService

class HomeRemoteDataSource() {
    private var _liveData = MutableLiveData<Group>()
    suspend fun getAllStudy(userId: Int) : LiveData<Group> {
        val response = RetrofitService.retrofitApi.getAllStudy(userId)
        if (response.isSuccessful){
            _liveData.value = response.body()
            return _liveData
        }
        return _liveData
    }
}