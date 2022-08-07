package com.example.swith.repository.create.round

import com.example.swith.data.Session
import com.example.swith.data.SessionResponse
import com.example.swith.utils.error.RemoteErrorEmitter


class RoundCreateRepository(private val roundCreateRemoteDataSource: RoundCreateRemoteDataSource) {
    suspend fun createRound(remoteErrorEmitter: RemoteErrorEmitter, session: Session) : SessionResponse? = roundCreateRemoteDataSource.createRound(remoteErrorEmitter, session)
}