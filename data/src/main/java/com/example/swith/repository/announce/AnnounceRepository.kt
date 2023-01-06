package com.example.swith.repository.announce

import com.example.swith.remote.announce.AnnounceRemoteDataSource
import com.example.swith.utils.RemoteErrorEmitter

class AnnounceRepository(private val announceRemoteDataSource: AnnounceRemoteDataSource) {
    suspend fun getAllAnnounce(
        errorEmitter: RemoteErrorEmitter,
        groupIdx: Long
    ): com.example.swith.entity.AnnounceList? =
        announceRemoteDataSource.getAllAnnounce(errorEmitter, groupIdx)

    suspend fun deleteAnnounce(emitter: RemoteErrorEmitter, announcementIdx: Long) =
        announceRemoteDataSource.deleteAnnounce(emitter, announcementIdx)

    suspend fun createAnnounce(
        emitter: RemoteErrorEmitter,
        announceCreate: com.example.swith.entity.AnnounceCreate
    ) = announceRemoteDataSource.createAnnounce(emitter, announceCreate)

    suspend fun updateAnnounce(
        emitter: RemoteErrorEmitter,
        announceModify: com.example.swith.entity.AnnounceModify
    ) = announceRemoteDataSource.updateAnnounce(emitter, announceModify)
}