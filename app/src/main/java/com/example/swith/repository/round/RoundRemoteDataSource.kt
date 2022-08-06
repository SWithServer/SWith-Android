package com.example.swith.repository.round

import com.example.swith.data.Round
import com.example.swith.repository.BaseRepository
import com.example.swith.repository.RetrofitService.retrofitApi

class RoundRemoteDataSource : BaseRepository() {
    suspend fun getAllRound(userIdx: Int, groupIdx: Int) : Round?{
        return safeApiCall { retrofitApi.getAllRound(userIdx, groupIdx).body()?.round }
    }
}