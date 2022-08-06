package com.example.swith.repository.home

import com.example.swith.data.GroupList
import com.example.swith.repository.RetrofitService.retrofitApi
import com.example.swith.repository.BaseRepository

class HomeRemoteDataSource() : BaseRepository(){
    suspend fun getAllStudy(userId: Int) : GroupList? {
        return safeApiCall { retrofitApi.getAllStudy(userId).body()}
    }
}