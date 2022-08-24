package com.example.swith.repository.manage.attend

import com.example.swith.data.UpdateAttend
import com.example.swith.utils.error.RemoteErrorEmitter

class ManageAttendRepository(private val remoteDataSource: ManageAttendRemoteDataSource) {
    suspend fun getAttendData(emitter: RemoteErrorEmitter, groupIdx: Long) = remoteDataSource.getAttendData(emitter, groupIdx)
    suspend fun updateAttendData(emitter: RemoteErrorEmitter, attendList: List<UpdateAttend>) = remoteDataSource.updateAttendData(emitter, attendList)
}