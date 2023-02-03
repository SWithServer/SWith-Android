package com.example.swith.data.remote.home

import com.example.swith.data.api.SwithService
import com.example.swith.data.di.DispatcherModule.IoDispatcher
import com.example.swith.domain.entity.Group
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    private val swithService: SwithService,
    @IoDispatcher dispatcher: CoroutineDispatcher
) {
    fun loadHomeData(
        userIdx: Long
    ) : Flow<List<Group>> = flow {
        emit(swithService.getAllStudy(userIdx).group)
    }
}

