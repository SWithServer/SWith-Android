package com.example.swith.data.repository.home

import com.example.swith.data.remote.home.HomeRemoteDataSource
import com.example.swith.domain.entity.Group
import com.example.swith.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeRemoteDataSource: HomeRemoteDataSource
) : HomeRepository{
    override fun loadHomeData(
        userIdx: Long
    ): Flow<List<Group>> = homeRemoteDataSource.loadHomeData(userIdx)
}