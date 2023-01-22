package com.example.swith.domain.entity

import com.google.gson.annotations.SerializedName

data class AttendInfo(
    val getAttendanceInfos: List<GetAttendanceInfo>,
    val sessionIdx: Long,
    val sessionNum: Int,
)

data class GetAttendanceInfo(
    val attendanceIdx: Long,
    val nickname: String,
    var status: Int,
)

data class AttendList(
    @SerializedName("result") val attend: ArrayList<AttendInfo>,
)

data class UpdateAttend(
    val attendanceIdx: Long,
    var status: Int,
)