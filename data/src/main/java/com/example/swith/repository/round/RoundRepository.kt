package com.example.swith.repository.round

import com.example.swith.remote.round.RoundRemoteDataSource
import com.example.swith.utils.RemoteErrorEmitter

class RoundRepository(private val roundRemoteDataSource: RoundRemoteDataSource) {
    suspend fun getAllRound(errorEmitter: RemoteErrorEmitter, userIdx: Long, groupIdx: Long) =
        roundRemoteDataSource.getAllRound(errorEmitter, userIdx, groupIdx)

    suspend fun getSessionInfo(emitter: RemoteErrorEmitter, userIdx: Long, sessionIdx: Long) =
        roundRemoteDataSource.getSessionInfo(emitter, userIdx, sessionIdx)

    suspend fun updateAttend(emitter: RemoteErrorEmitter, userIdx: Long, sessionIdx: Long) =
        roundRemoteDataSource.updateAttend(emitter, userIdx, sessionIdx)

    suspend fun getUserAttend(emitter: RemoteErrorEmitter, groupIdX: Long) =
        roundRemoteDataSource.getUserAttend(emitter, groupIdX)

    suspend fun createMemo(emitter: RemoteErrorEmitter, memo: com.example.swith.entity.Memo) =
        roundRemoteDataSource.createMemo(emitter, memo)

    suspend fun updateMemo(
        emitter: RemoteErrorEmitter,
        memoUpdate: com.example.swith.entity.MemoUpdate
    ) = roundRemoteDataSource.updateMemo(emitter, memoUpdate)
}