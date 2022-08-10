package com.example.swith.repository.round

import android.util.Log
import com.example.swith.data.AttendResponse
import com.example.swith.data.Round
import com.example.swith.data.SessionInfo
import com.example.swith.utils.base.BaseRepository
import com.example.swith.repository.RetrofitService.retrofitApi
import com.example.swith.utils.error.RemoteErrorEmitter

class RoundRemoteDataSource : BaseRepository() {
    suspend fun getAllRound(errorEmitter: RemoteErrorEmitter, userIdx: Int, groupIdx: Int) : Round?{
        return safeApiCall(errorEmitter) { retrofitApi.getAllRound(userIdx, groupIdx).body()?.round }
    }

    suspend fun getSessionInfo(emitter: RemoteErrorEmitter, userIdx: Int, sessionIdx: Int) : SessionInfo? {
        return safeApiCall(emitter){retrofitApi.getSessionInfo(userIdx, sessionIdx).body()?.session}
    }

    suspend fun updateAttend(emitter: RemoteErrorEmitter, userIdx: Int, sessionIdx: Int) : AttendResponse? {
        return safeApiCall(emitter){ retrofitApi.updateAttend(userIdx, sessionIdx).body()}
    }
}