package com.example.swith.data.repository.round

import com.example.swith.data.remote.round.RoundRemoteDataSource
import com.example.swith.domain.utils.RemoteErrorEmitter
import com.example.swith.domain.entity.Memo
import com.example.swith.domain.entity.MemoUpdate

class RoundRepository(private val roundRemoteDataSource: RoundRemoteDataSource) {
    suspend fun getAllRound(errorEmitter: RemoteErrorEmitter, userIdx: Long, groupIdx: Long) =
        roundRemoteDataSource.getAllRound(errorEmitter, userIdx, groupIdx)

    suspend fun getSessionInfo(emitter: RemoteErrorEmitter, userIdx: Long, sessionIdx: Long) =
        roundRemoteDataSource.getSessionInfo(emitter, userIdx, sessionIdx)

    suspend fun updateAttend(emitter: RemoteErrorEmitter, userIdx: Long, sessionIdx: Long) =
        roundRemoteDataSource.updateAttend(emitter, userIdx, sessionIdx)

    suspend fun getUserAttend(emitter: RemoteErrorEmitter, groupIdX: Long) =
        roundRemoteDataSource.getUserAttend(emitter, groupIdX)

    suspend fun createMemo(emitter: RemoteErrorEmitter, memo: Memo) =
        roundRemoteDataSource.createMemo(emitter, memo)

    suspend fun updateMemo(
        emitter: RemoteErrorEmitter,
        memoUpdate: MemoUpdate
    ) = roundRemoteDataSource.updateMemo(emitter, memoUpdate)
}