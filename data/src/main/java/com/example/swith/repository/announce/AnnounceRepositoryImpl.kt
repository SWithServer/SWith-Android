package com.example.swith.repository.announce

import com.example.swith.entity.AnnounceCreate
import com.example.swith.entity.AnnounceList
import com.example.swith.entity.AnnounceModify
import com.example.swith.remote.announce.AnnounceRemoteDataSource
import com.example.swith.repository.AnnounceRepository
import com.example.swith.utils.RemoteErrorEmitter
import javax.inject.Inject

class AnnounceRepositoryImpl @Inject constructor(
    private val announceDataSource : AnnounceRemoteDataSource
) : AnnounceRepository {
    override suspend fun getAllAnnounce(
        errorEmitter: RemoteErrorEmitter,
        groupIdx: Long,
    ): AnnounceList? {
        return announceDataSource.getAllAnnounce(errorEmitter, groupIdx)
    }

    override suspend fun deleteAnnounce(
        emitter: RemoteErrorEmitter,
        announcementIdx: Long,
    ): String? {
        return announceDataSource.deleteAnnounce(emitter, announcementIdx)
    }

    override suspend fun createAnnounce(
        emitter: RemoteErrorEmitter,
        announceCreate: AnnounceCreate,
    ): Any? {
        return announceDataSource.createAnnounce(emitter, announceCreate)
    }

    override suspend fun updateAnnounce(
        emitter: RemoteErrorEmitter,
        announceModify: AnnounceModify,
    ): Any? {
        return announceDataSource.updateAnnounce(emitter, announceModify)
    }
}