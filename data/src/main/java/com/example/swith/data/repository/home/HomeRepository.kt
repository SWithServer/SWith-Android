package com.example.swith.data.repository.home

import com.example.swith.data.remote.home.HomeRemoteDataSource
import com.example.swith.domain.utils.RemoteErrorEmitter
import com.example.swith.domain.entity.GroupList

class HomeRepository(private val homeRemoteDataSource: HomeRemoteDataSource) {
    suspend fun getAllStudy(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: Long
    ): GroupList? =
        homeRemoteDataSource.getAllStudy(remoteErrorEmitter, userId)
//    suspend fun getRegPlace(remoteErrorEmitter: RemoteErrorEmitter, regionIdx: Long) = homeRemoteDataSource.getRegPlace(remoteErrorEmitter, regionIdx)
}