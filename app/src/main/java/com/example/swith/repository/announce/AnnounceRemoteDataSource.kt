package com.example.swith.repository.announce

import com.example.swith.data.AnnounceList
import com.example.swith.repository.RetrofitService.retrofitApi
import com.example.swith.utils.base.BaseRepository
import com.example.swith.utils.error.RemoteErrorEmitter

class AnnounceRemoteDataSource : BaseRepository(){
    suspend fun getAllAnnounce(errorEmitter: RemoteErrorEmitter, groupIdx: Int) : AnnounceList? {
        return safeApiCall(errorEmitter){retrofitApi.getAllAnnounce(groupIdx).body()}
    }

    suspend fun deleteAnnounce(emitter: RemoteErrorEmitter, announcementIdx: Int) : String?{
        return safeApiCall(emitter){retrofitApi.deleteAnnounce(announcementIdx).let {
            if (it.isSuccessful) it.body()
            else null
        }}
    }
}