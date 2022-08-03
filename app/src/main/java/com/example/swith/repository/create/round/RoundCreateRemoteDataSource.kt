package com.example.swith.repository.create.round

import android.util.Log
import com.example.swith.data.Session
import com.example.swith.data.SessionResponse
import com.example.swith.repository.BaseRepository
import com.example.swith.repository.RetrofitService.retrofitApi

class RoundCreateRemoteDataSource : BaseRepository() {
    suspend fun createRound(session: Session) : SessionResponse?{
        return safeApiCall { retrofitApi.createRound(session).also { Log.e("response", it.toString()) }.body() }
    }
}