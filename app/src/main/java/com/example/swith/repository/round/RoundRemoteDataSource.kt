package com.example.swith.repository.round

import com.example.swith.data.Round
import com.example.swith.utils.base.BaseRepository
import com.example.swith.repository.RetrofitService.retrofitApi
import com.example.swith.utils.error.RemoteErrorEmitter

class RoundRemoteDataSource : BaseRepository() {
    suspend fun getAllRound(errorEmitter: RemoteErrorEmitter, userIdx: Int, groupIdx: Int) : Round?{
        return safeApiCall(errorEmitter) { retrofitApi.getAllRound(userIdx, groupIdx).body()?.round }
    }
}