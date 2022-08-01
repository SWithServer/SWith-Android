package com.example.swith.repository.home

import androidx.lifecycle.MutableLiveData
import com.example.swith.repository.RetrofitService.retrofitApi
import com.example.swith.data.Group
import com.example.swith.repository.BaseRepository

class HomeRemoteDataSource() : BaseRepository(){
    suspend fun getAllStudy(userId: Int) : Group? {
        return safeApiCall { retrofitApi.getAllStudy(userId).body()}
    }
}