package com.example.swith.repository.create.round

import android.util.Log
import com.example.swith.data.Session
import com.example.swith.data.SessionResponse
import com.example.swith.utils.base.BaseRepository
import com.example.swith.repository.RetrofitService.retrofitApi
import com.example.swith.utils.error.RemoteErrorEmitter

class RoundCreateRemoteDataSource : BaseRepository() {
    suspend fun createRound(errorEmitter: RemoteErrorEmitter, session: Session) : SessionResponse?{
        return safeApiCall(errorEmitter) { retrofitApi.createRound(session).also { Log.e("response", it.toString()) }.body() }
    }
}