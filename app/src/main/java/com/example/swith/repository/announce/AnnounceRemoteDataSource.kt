package com.example.swith.repository.announce

import com.example.swith.data.AnnounceList
import com.example.swith.repository.RetrofitService.retrofitApi
import com.example.swith.utils.base.BaseRepository
import com.example.swith.utils.error.RemoteErrorEmitter

class AnnounceRemoteDataSource : BaseRepository(){
    suspend fun getAllAnnounce(errorEmitter: RemoteErrorEmitter, groupIdx: Int) : AnnounceList? {
        return safeApiCall(errorEmitter){retrofitApi.getAllAnnounce(groupIdx).body()}
    }
}