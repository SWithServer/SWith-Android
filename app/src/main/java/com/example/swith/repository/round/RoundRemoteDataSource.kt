package com.example.swith.repository.round

import android.util.Log
import com.example.swith.data.*
import com.example.swith.utils.base.BaseRepository
import com.example.swith.repository.RetrofitService.retrofitApi
import com.example.swith.utils.error.RemoteErrorEmitter

class RoundRemoteDataSource : BaseRepository() {
    suspend fun getAllRound(errorEmitter: RemoteErrorEmitter, userIdx: Long, groupIdx: Long) : Round?{
        return safeApiCall(errorEmitter) { retrofitApi.getAllRound(userIdx, groupIdx).body()?.round }
    }

    suspend fun getSessionInfo(emitter: RemoteErrorEmitter, userIdx: Long, sessionIdx: Long) : SessionInfo? {
        return safeApiCall(emitter){retrofitApi.getSessionInfo(userIdx, sessionIdx).body()?.session}
    }

    suspend fun updateAttend(emitter: RemoteErrorEmitter, userIdx: Long, sessionIdx: Long) : AttendResponse? {
        return safeApiCall(emitter){ retrofitApi.updateAttend(userIdx, sessionIdx).body()}
    }

    suspend fun getUserAttend(emitter: RemoteErrorEmitter, groupIdx: Long) : UserAttend? {
        return safeApiCall(emitter){ retrofitApi.getUserAttend(groupIdx).body()?.attend}
    }

    suspend fun createMemo(emitter: RemoteErrorEmitter, memo: Memo) : MemoResponse?{
        return safeApiCall(emitter){ retrofitApi.createMemo(memo).body()}
    }

    suspend fun updateMemo(emitter: RemoteErrorEmitter, memoUpdate: MemoUpdate) : MemoResponse? {
        return safeApiCall(emitter){ retrofitApi.updateMemo(memoUpdate).body()}
    }
}