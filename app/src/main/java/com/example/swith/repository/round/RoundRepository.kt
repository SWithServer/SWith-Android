package com.example.swith.repository.round

import com.example.swith.utils.error.RemoteErrorEmitter

class RoundRepository(private val roundRemoteDataSource: RoundRemoteDataSource){
    suspend fun getAllRound(errorEmitter: RemoteErrorEmitter, userIdx: Int ,groupIdx: Int) = roundRemoteDataSource.getAllRound(errorEmitter, userIdx, groupIdx)
}