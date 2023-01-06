package com.example.swith.repository.manage.attend

import com.example.swith.repository.RetrofitService.retrofitApi
import com.example.swith.utils.BaseRepository
import com.example.swith.utils.RemoteErrorEmitter


class ManageAttendRemoteDataSource : BaseRepository() {
    suspend fun getAttendData(
        emitter: RemoteErrorEmitter,
        groupIdx: Long
    ): com.example.swith.entity.AttendList? {
        return safeApiCall(emitter) { retrofitApi.getAttendData(groupIdx).body() }
    }

    suspend fun updateAttendData(
        emitter: RemoteErrorEmitter,
        attendList: List<com.example.swith.entity.UpdateAttend>
    ): com.example.swith.entity.AttendResponse? {
        return safeApiCall(emitter) { retrofitApi.updateAttendData(attendList).body() }
    }
}