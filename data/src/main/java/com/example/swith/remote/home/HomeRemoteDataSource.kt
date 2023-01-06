package com.example.swith.remote.home

import com.example.swith.repository.RetrofitService.retrofitApi
import com.example.swith.utils.BaseRepository
import com.example.swith.utils.RemoteErrorEmitter

class HomeRemoteDataSource() : BaseRepository() {
    suspend fun getAllStudy(
        errorEmitter: RemoteErrorEmitter,
        userId: Long
    ): com.example.swith.entity.GroupList? {
        return safeApiCall(errorEmitter) {
            retrofitApi.getAllStudy(userId).let {
                if (it.body()?.isSuccess == true) it.body()
                else {
                    errorEmitter.onError(it.body()?.message!!)
                    null
                }
            }
        }
    }
}

