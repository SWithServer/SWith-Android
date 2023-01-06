package com.example.swith.repository.round.update

import com.example.swith.entity.Session
import com.example.swith.entity.SessionModify
import com.example.swith.remote.round.RoundUpdateRemoteDataSource
import com.example.swith.utils.RemoteErrorEmitter


class RoundUpdateRepository(private val roundUpdateRemoteDataSource: RoundUpdateRemoteDataSource) {
    suspend fun getPostRound(errorEmitter: RemoteErrorEmitter, userIdx: Long, groupIdx: Long) =
        roundUpdateRemoteDataSource.getPostRound(errorEmitter, userIdx, groupIdx)

    suspend fun createRound(remoteErrorEmitter: RemoteErrorEmitter, session: Session) =
        roundUpdateRemoteDataSource.createRound(remoteErrorEmitter, session)

    suspend fun deleteRound(remoteErrorEmitter: RemoteErrorEmitter, sessionIdx: Long) =
        roundUpdateRemoteDataSource.deleteRound(remoteErrorEmitter, sessionIdx)

    suspend fun modifyRound(remoteErrorEmitter: RemoteErrorEmitter, session: SessionModify) =
        roundUpdateRemoteDataSource.modifyRound(remoteErrorEmitter, session)
}