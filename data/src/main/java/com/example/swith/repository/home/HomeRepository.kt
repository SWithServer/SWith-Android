package com.example.swith.repository.home

import com.example.swith.remote.home.HomeRemoteDataSource
import com.example.swith.utils.RemoteErrorEmitter

class HomeRepository(private val homeRemoteDataSource: HomeRemoteDataSource) {
    suspend fun getAllStudy(
        remoteErrorEmitter: RemoteErrorEmitter,
        userId: Long
    ): com.example.swith.entity.GroupList? =
        homeRemoteDataSource.getAllStudy(remoteErrorEmitter, userId)
//    suspend fun getRegPlace(remoteErrorEmitter: RemoteErrorEmitter, regionIdx: Long) = homeRemoteDataSource.getRegPlace(remoteErrorEmitter, regionIdx)
}