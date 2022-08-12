package com.example.swith.repository.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swith.data.Group
import com.example.swith.data.GroupList
import com.example.swith.repository.RetrofitService
import com.example.swith.utils.error.RemoteErrorEmitter
import kotlinx.coroutines.flow.Flow

class HomeRepository(private val homeRemoteDataSource: HomeRemoteDataSource){
    suspend fun getAllStudy(remoteErrorEmitter: RemoteErrorEmitter, userId: Int) : GroupList? = homeRemoteDataSource.getAllStudy(remoteErrorEmitter, userId)
//    suspend fun getRegPlace(remoteErrorEmitter: RemoteErrorEmitter, regionIdx: Long) = homeRemoteDataSource.getRegPlace(remoteErrorEmitter, regionIdx)
}