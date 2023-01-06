package com.example.swith.repository.manage.attend

import com.example.swith.utils.RemoteErrorEmitter


class ManageAttendRepository(private val remoteDataSource: ManageAttendRemoteDataSource) {
    suspend fun getAttendData(emitter: RemoteErrorEmitter, groupIdx: Long) =
        remoteDataSource.getAttendData(emitter, groupIdx)

    suspend fun updateAttendData(
        emitter: RemoteErrorEmitter,
        attendList: List<com.example.swith.entity.UpdateAttend>
    ) = remoteDataSource.updateAttendData(emitter, attendList)
}