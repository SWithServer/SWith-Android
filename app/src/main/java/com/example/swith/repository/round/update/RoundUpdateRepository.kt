package com.example.swith.repository.round.update

import com.example.swith.data.Session
import com.example.swith.data.SessionCreate
import com.example.swith.data.SessionResponse
import com.example.swith.utils.error.RemoteErrorEmitter


class RoundUpdateRepository(private val roundUpdateRemoteDataSource: RoundUpdateRemoteDataSource) {
    suspend fun createRound(remoteErrorEmitter: RemoteErrorEmitter, session: Session) : SessionCreate? = roundUpdateRemoteDataSource.createRound(remoteErrorEmitter, session)
    suspend fun deleteRound(remoteErrorEmitter: RemoteErrorEmitter, sessionIdx: Int) : SessionResponse? = roundUpdateRemoteDataSource.deleteRound(remoteErrorEmitter, sessionIdx)
}