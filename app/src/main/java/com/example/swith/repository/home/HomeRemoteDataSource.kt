package com.example.swith.repository.home

import android.util.Log
import com.example.swith.repository.RetrofitService.retrofitApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swith.data.Group

class HomeRemoteDataSource() {
    private var _liveData = MutableLiveData<Group>()
    suspend fun getAllStudy(userId: Int) : LiveData<Group> {
//        val response = retrofitApi.getAllStudy(userId)
//        Log.e("response", response.toString())
//        if (response.isSuccessful){
//            _liveData.value = response.body()
//            return _liveData
//        }
        return _liveData
    }
}