package com.example.swith.data.repository.manage.attend

import com.example.swith.domain.utils.RemoteErrorEmitter
import com.example.swith.domain.entity.UpdateAttend


class ManageAttendRepository(private val remoteDataSource: ManageAttendRemoteDataSource) {
    suspend fun getAttendData(emitter: RemoteErrorEmitter, groupIdx: Long) =
        remoteDataSource.getAttendData(emitter, groupIdx)

    suspend fun updateAttendData(
        emitter: RemoteErrorEmitter,
        attendList: List<UpdateAttend>
    ) = remoteDataSource.updateAttendData(emitter, attendList)
}