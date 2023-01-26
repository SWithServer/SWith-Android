package com.example.swith.domain.repository


import com.example.swith.domain.entity.AnnounceCreate
import com.example.swith.domain.entity.AnnounceList
import com.example.swith.domain.entity.AnnounceModify
import com.example.swith.domain.utils.RemoteErrorEmitter

interface AnnounceRepository {
    suspend fun getAllAnnounce(errorEmitter: RemoteErrorEmitter, groupIdx: Long): AnnounceList?
    suspend fun deleteAnnounce(emitter: RemoteErrorEmitter, announcementIdx: Long): String?

    suspend fun createAnnounce(emitter: RemoteErrorEmitter, announceCreate: AnnounceCreate): Any?
    suspend fun updateAnnounce(emitter: RemoteErrorEmitter, announceModify: AnnounceModify): Any?
}