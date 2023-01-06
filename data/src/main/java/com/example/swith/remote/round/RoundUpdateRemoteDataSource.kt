package com.example.swith.remote.round

import com.example.swith.entity.*
import com.example.swith.repository.RetrofitService.retrofitApi
import com.example.swith.utils.BaseRepository
import com.example.swith.utils.RemoteErrorEmitter
import java.time.ZoneId
import java.time.ZonedDateTime

class RoundUpdateRemoteDataSource : BaseRepository() {
    suspend fun getPostRound(
        errorEmitter: RemoteErrorEmitter,
        userIdx: Long,
        groupIdx: Long
    ): Round? {
        val tempList = ArrayList<GetSessionRes>()
        return safeApiCall(errorEmitter) {
            retrofitApi.getAllRound(userIdx, groupIdx).body().let {
                it?.round?.getSessionResList?.forEach { s ->
                    if (compareTimeWithNow(s.sessionStart)) tempList.add(s)
                }
                if (tempList.isNotEmpty()) tempList.sortBy { t -> t.sessionNum }
                it?.round.apply { it?.round?.getSessionResList = tempList }
            }
        }
    }

    suspend fun createRound(errorEmitter: RemoteErrorEmitter, session: Session): SessionCreate? {
        return safeApiCall(errorEmitter) {
            retrofitApi.createRound(session).let {
                if (it.body()?.isSuccess == true) it.body()
                else {
                    errorEmitter.onError(it.body()?.message!!)
                    null
                }
            }
        }

    }

    suspend fun deleteRound(errorEmitter: RemoteErrorEmitter, sessionIdx: Long): SessionResponse? {
        return safeApiCall(errorEmitter) {
            retrofitApi.deleteRound(sessionIdx).let {
                if (it.body()?.isSuccess == true) it.body()
                else {
                    errorEmitter.onError(it.body()?.message!!)
                    null
                }
            }
        }
    }

    suspend fun modifyRound(
        errorEmitter: RemoteErrorEmitter,
        session: SessionModify
    ): SessionResponse? {
        return safeApiCall(errorEmitter) {
            retrofitApi.modifyRound(session).let {
                if (it.body()?.isSuccess == true) it.body()
                else {
                    errorEmitter.onError(it.body()?.message!!)
                    null
                }
            }
        }
    }


    fun compareTimeWithNow(timeList: List<Int>): Boolean {
        with(ZonedDateTime.now(ZoneId.of("Asia/Seoul"))) {
            val nowTimeToLong =
                String.format("%4d%02d%02d%02d%02d", year, monthValue, dayOfMonth, hour, minute)
                    .toLong()
            val sessionTimeToLong = String.format(
                "%4d%02d%02d%02d%02d",
                timeList[0],
                timeList[1],
                timeList[2],
                timeList[3],
                timeList[4]
            ).toLong()
            if (sessionTimeToLong < nowTimeToLong) return false
        }
        return true
    }

}