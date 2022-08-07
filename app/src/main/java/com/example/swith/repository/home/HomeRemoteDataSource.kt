package com.example.swith.repository.home

import com.example.swith.data.GroupList
import com.example.swith.repository.RetrofitService.retrofitApi
import com.example.swith.utils.base.BaseRepository
import com.example.swith.utils.error.RemoteErrorEmitter

class HomeRemoteDataSource() : BaseRepository(){
    suspend fun getAllStudy(errorEmitter: RemoteErrorEmitter, userId: Int) : GroupList? {
        return safeApiCall(errorEmitter) { retrofitApi.getAllStudy(userId).body()}
    }
}