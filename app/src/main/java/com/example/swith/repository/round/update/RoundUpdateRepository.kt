package com.example.swith.repository.round.update

import com.example.swith.data.Session
import com.example.swith.data.SessionCreate
import com.example.swith.data.SessionModify
import com.example.swith.data.SessionResponse
import com.example.swith.utils.error.RemoteErrorEmitter


class RoundUpdateRepository(private val roundUpdateRemoteDataSource: RoundUpdateRemoteDataSource) {
    suspend fun getPostRound(errorEmitter: RemoteErrorEmitter, userIdx: Int ,groupIdx: Int) = roundUpdateRemoteDataSource.getPostRound(errorEmitter, userIdx, groupIdx)
    suspend fun createRound(remoteErrorEmitter: RemoteErrorEmitter, session: Session) = roundUpdateRemoteDataSource.createRound(remoteErrorEmitter, session)
    suspend fun deleteRound(remoteErrorEmitter: RemoteErrorEmitter, sessionIdx: Int) = roundUpdateRemoteDataSource.deleteRound(remoteErrorEmitter, sessionIdx)
    suspend fun modifyRound(remoteErrorEmitter: RemoteErrorEmitter, session: SessionModify) = roundUpdateRemoteDataSource.modifyRound(remoteErrorEmitter, session)
}