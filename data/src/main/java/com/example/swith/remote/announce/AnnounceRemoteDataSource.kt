package com.example.swith.remote.announce

import com.example.swith.api.SwithService
import com.example.swith.repository.RetrofitService.retrofitApi
import com.example.swith.utils.BaseRepository
import com.example.swith.utils.RemoteErrorEmitter
import javax.inject.Inject

class AnnounceRemoteDataSource @Inject constructor(
    private val swithService: SwithService
): BaseRepository() {
    // Todo : Any로 되어 있는겨 변경 할 예정
    // Todo : SafeApiCall 지울까 말까 고민

    suspend fun getAllAnnounce(
        errorEmitter: RemoteErrorEmitter,
        groupIdx: Long
    ): com.example.swith.entity.AnnounceList? {
        return safeApiCall(errorEmitter) { swithService.getAllAnnounce(groupIdx).body() }
    }

    suspend fun deleteAnnounce(emitter: RemoteErrorEmitter, announcementIdx: Long): String? {
        return safeApiCall(emitter) {
            swithService.deleteAnnounce(announcementIdx).let {
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
        return safeApiCall(emitter) { swithService.createAnnounce(announceCreate) }
    }

    suspend fun updateAnnounce(
        emitter: RemoteErrorEmitter,
        announceModify: com.example.swith.entity.AnnounceModify
    ): Any? {
        return safeApiCall(emitter) { swithService.updateAnnounce(announceModify) }
    }
}