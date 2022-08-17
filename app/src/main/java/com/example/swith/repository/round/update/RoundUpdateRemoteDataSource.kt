package com.example.swith.repository.round.update

import android.util.Log
import com.example.swith.data.*
import com.example.swith.utils.base.BaseRepository
import com.example.swith.repository.RetrofitService.retrofitApi
import com.example.swith.utils.compareTimeWithNow
import com.example.swith.utils.error.RemoteErrorEmitter

class RoundUpdateRemoteDataSource : BaseRepository() {
    suspend fun getPostRound(errorEmitter: RemoteErrorEmitter, userIdx: Int, groupIdx: Int) : Round? {
        val tempList = ArrayList<GetSessionRes>()
        return safeApiCall(errorEmitter){ retrofitApi.getAllRound(userIdx, groupIdx).body().let {
            it?.round?.getSessionResList?.forEach { s ->
                if (compareTimeWithNow(s.sessionStart)) tempList.add(s)
            }
            if(tempList.isNotEmpty()) tempList.sortBy { t -> t.sessionNum }
            it?.round.apply { it?.round?.getSessionResList = tempList }
        }}
    }

    suspend fun createRound(errorEmitter: RemoteErrorEmitter, session: Session) : SessionCreate?{
        return safeApiCall(errorEmitter) { retrofitApi.createRound(session).let {
            if (it.body()?.isSuccess == true) it.body()
            else {
                errorEmitter.onError(it.body()?.message!!)
                null
            }
        }}

    }
    suspend fun deleteRound(errorEmitter: RemoteErrorEmitter, sessionIdx: Int) : SessionResponse?{
        return safeApiCall(errorEmitter) { retrofitApi.deleteRound(sessionIdx).let {
            if (it.body()?.isSuccess == true) it.body()
            else{
                errorEmitter.onError(it.body()?.message!!)
                null
            }
        }}
    }

    suspend fun modifyRound(errorEmitter: RemoteErrorEmitter, session: SessionModify) : SessionResponse?{
        return safeApiCall(errorEmitter) { retrofitApi.modifyRound(session).let {
            if (it.body()?.isSuccess == true) it.body()
            else {
                errorEmitter.onError(it.body()?.message!!)
                null
            }
        }}
    }
}