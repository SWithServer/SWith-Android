package com.example.swith.repository.manage.attend

import com.example.swith.data.AttendList
import com.example.swith.data.AttendResponse
import com.example.swith.data.UpdateAttend
import com.example.swith.repository.RetrofitService.retrofitApi
import com.example.swith.utils.base.BaseRepository
import com.example.swith.utils.error.RemoteErrorEmitter

class ManageAttendRemoteDataSource : BaseRepository() {
    suspend fun getAttendData(emitter: RemoteErrorEmitter, groupIdx: Long) : AttendList? {
        return safeApiCall(emitter) { retrofitApi.getAttendData(groupIdx).body()}
    }

    suspend fun updateAttendData(emitter: RemoteErrorEmitter, attendList: List<UpdateAttend>) : AttendResponse?{
        return safeApiCall(emitter) { retrofitApi.updateAttendData(attendList).body()}
    }
}