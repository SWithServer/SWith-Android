package com.example.swith.repository.round

class RoundRepository(private val roundRemoteDataSource: RoundRemoteDataSource){
    suspend fun getAllRound(userIdx: Int, groupIdx: Int) = roundRemoteDataSource.getAllRound(userIdx, groupIdx)
}