package com.example.swith.repository.home

import com.example.swith.data.GroupList
import com.example.swith.repository.ApiService
import com.example.swith.repository.RetrofitService.REG_CODE
import com.example.swith.repository.RetrofitService.retrofitApi
import com.example.swith.utils.base.BaseRepository
import com.example.swith.utils.error.RemoteErrorEmitter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeRemoteDataSource() : BaseRepository(){
    suspend fun getAllStudy(errorEmitter: RemoteErrorEmitter, userId: Long) : GroupList? {
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

