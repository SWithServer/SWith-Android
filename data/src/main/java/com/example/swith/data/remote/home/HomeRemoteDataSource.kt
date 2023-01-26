package com.example.swith.data.remote.home

import com.example.swith.data.api.RetrofitService.retrofitApi
import com.example.swith.data.utils.BaseRepository
import com.example.swith.domain.utils.RemoteErrorEmitter
import com.example.swith.domain.entity.GroupList

class HomeRemoteDataSource() : BaseRepository() {
    suspend fun getAllStudy(
        errorEmitter: RemoteErrorEmitter,
        userId: Long
    ): GroupList? {
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

