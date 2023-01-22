package com.example.swith.data.repository.manage.attend

import com.example.swith.data.api.RetrofitService.retrofitApi
import com.example.swith.data.utils.BaseRepository
import com.example.swith.domain.utils.RemoteErrorEmitter
import com.example.swith.domain.entity.AttendList
import com.example.swith.domain.entity.AttendResponse
import com.example.swith.domain.entity.UpdateAttend


class ManageAttendRemoteDataSource : BaseRepository() {
    suspend fun getAttendData(
        emitter: RemoteErrorEmitter,
        groupIdx: Long
    ): AttendList? {
        return safeApiCall(emitter) { retrofitApi.getAttendData(groupIdx).body() }
    }

    suspend fun updateAttendData(
        emitter: RemoteErrorEmitter,
        attendList: List<UpdateAttend>
    ): AttendResponse? {
        return safeApiCall(emitter) { retrofitApi.updateAttendData(attendList).body() }
    }
}