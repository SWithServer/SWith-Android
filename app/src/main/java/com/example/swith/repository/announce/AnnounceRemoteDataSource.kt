package com.example.swith.repository.announce

import com.example.swith.data.AnnounceCreate
import com.example.swith.data.AnnounceList
import com.example.swith.data.AnnounceModify
import com.example.swith.repository.RetrofitService.retrofitApi
import com.example.swith.utils.base.BaseRepository
import com.example.swith.utils.error.RemoteErrorEmitter

class AnnounceRemoteDataSource : BaseRepository(){
    suspend fun getAllAnnounce(errorEmitter: RemoteErrorEmitter, groupIdx: Int) : AnnounceList? {
        return safeApiCall(errorEmitter){retrofitApi.getAllAnnounce(groupIdx).body()}
    }

    suspend fun deleteAnnounce(emitter: RemoteErrorEmitter, announcementIdx: Int) : String?{
        return safeApiCall(emitter){retrofitApi.deleteAnnounce(announcementIdx).let {
            if (it.body()?.isSuccess == true) it.body()?.result
            else {
                emitter.onError(it.body()?.message!!)
                null
            }
        }}
    }

    suspend fun createAnnounce(emitter: RemoteErrorEmitter, announceCreate: AnnounceCreate) : Any?{
        return safeApiCall(emitter){ retrofitApi.createAnnounce(announceCreate)}
    }

    suspend fun updateAnnounce(emitter: RemoteErrorEmitter, announceModify: AnnounceModify) : Any?{
        return safeApiCall(emitter){ retrofitApi.updateAnnounce(announceModify)}
    }
}