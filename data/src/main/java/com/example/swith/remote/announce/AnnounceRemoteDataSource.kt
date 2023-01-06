package com.example.swith.remote.announce

import com.example.swith.repository.RetrofitService.retrofitApi
import com.example.swith.utils.BaseRepository
import com.example.swith.utils.RemoteErrorEmitter

class AnnounceRemoteDataSource : BaseRepository() {
    suspend fun getAllAnnounce(
        errorEmitter: RemoteErrorEmitter,
        groupIdx: Long
    ): com.example.swith.entity.AnnounceList? {
        return safeApiCall(errorEmitter) { retrofitApi.getAllAnnounce(groupIdx).body() }
    }

    suspend fun deleteAnnounce(emitter: RemoteErrorEmitter, announcementIdx: Long): String? {
        return safeApiCall(emitter) {
            retrofitApi.deleteAnnounce(announcementIdx).let {
                if (it.body()?.isSuccess == true) it.body()?.result
                else {
                    emitter.onError(it.body()?.message!!)
                    null
                }
            }
        }
    }

    suspend fun createAnnounce(
        emitter: RemoteErrorEmitter,
        announceCreate: com.example.swith.entity.AnnounceCreate
    ): Any? {
        return safeApiCall(emitter) { retrofitApi.createAnnounce(announceCreate) }
    }

    suspend fun updateAnnounce(
        emitter: RemoteErrorEmitter,
        announceModify: com.example.swith.entity.AnnounceModify
    ): Any? {
        return safeApiCall(emitter) { retrofitApi.updateAnnounce(announceModify) }
    }
}