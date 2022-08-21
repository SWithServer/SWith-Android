package com.example.swith.data

import com.google.gson.annotations.SerializedName

data class AttendInfo(
    val getAttendanceInfos: List<GetAttendanceInfo>,
    val sessionIdx: Int,
    val sessionNum: Int
)

data class GetAttendanceInfo(
    val attendanceIdx: Int,
    val nickname: String,
    var status: Int
)

data class AttendList(
    @SerializedName("result") val attend: ArrayList<AttendInfo>
)

data class UpdateAttend(
    val attendanceIdx: Int,
    var status: Int
)