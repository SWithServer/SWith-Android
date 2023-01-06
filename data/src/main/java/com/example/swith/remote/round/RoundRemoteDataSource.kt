package com.example.swith.remote.round

import com.example.swith.entity.Round
import com.example.swith.entity.SessionInfo
import com.example.swith.entity.UserAttend
import com.example.swith.repository.RetrofitService.retrofitApi
import com.example.swith.utils.BaseRepository
import com.example.swith.utils.RemoteErrorEmitter

class RoundRemoteDataSource : BaseRepository() {
    suspend fun getAllRound(
        errorEmitter: RemoteErrorEmitter,
        userIdx: Long,
        groupIdx: Long
    ): Round? {
        return safeApiCall(errorEmitter) {
            retrofitApi.getAllRound(userIdx, groupIdx).body()?.round
        }
    }

    suspend fun getSessionInfo(
        emitter: RemoteErrorEmitter,
        userIdx: Long,
        sessionIdx: Long
    ): SessionInfo? {
        return safeApiCall(emitter) {
            retrofitApi.getSessionInfo(userIdx, sessionIdx).body()?.session
        }
    }

    suspend fun updateAttend(
        emitter: RemoteErrorEmitter,
        userIdx: Long,
        sessionIdx: Long
    ): com.example.swith.entity.AttendResponse? {
        return safeApiCall(emitter) { retrofitApi.updateAttend(userIdx, sessionIdx).body() }
    }

    suspend fun getUserAttend(emitter: RemoteErrorEmitter, groupIdx: Long): UserAttend? {
        return safeApiCall(emitter) { retrofitApi.getUserAttend(groupIdx).body()?.attend }
    }

    suspend fun createMemo(
        emitter: RemoteErrorEmitter,
        memo: com.example.swith.entity.Memo
    ): com.example.swith.entity.MemoResponse? {
        return safeApiCall(emitter) { retrofitApi.createMemo(memo).body() }
    }

    suspend fun updateMemo(
        emitter: RemoteErrorEmitter,
        memoUpdate: com.example.swith.entity.MemoUpdate
    ): com.example.swith.entity.MemoResponse? {
        return safeApiCall(emitter) { retrofitApi.updateMemo(memoUpdate).body() }
    }
}