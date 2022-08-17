package com.example.swith.repository.round.update

import android.util.Log
import com.example.swith.data.Session
import com.example.swith.data.SessionCreate
import com.example.swith.data.SessionResponse
import com.example.swith.utils.base.BaseRepository
import com.example.swith.repository.RetrofitService.retrofitApi
import com.example.swith.utils.error.RemoteErrorEmitter

class RoundUpdateRemoteDataSource : BaseRepository() {
    suspend fun createRound(errorEmitter: RemoteErrorEmitter, session: Session) : SessionCreate?{
        return safeApiCall(errorEmitter) { retrofitApi.createRound(session).let {
            if (it.body()?.isSuccess == true) it.body()
            else {
                errorEmitter.onError(it.body()?.message!!)
                null
            }
        }}

    }
    suspend fun deleteRound(errorEmitter: RemoteErrorEmitter, sessionIdx: Int) : SessionResponse?{
        return safeApiCall(errorEmitter) { retrofitApi.deleteRound(sessionIdx).body()}
    }
}