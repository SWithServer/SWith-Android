package com.example.swith.entity

import com.google.gson.annotations.SerializedName

data class SessionInfo(
    val attendanceValidTime: Int,
    var getAttendanceList: List<GetAttendance>,
    val groupImgUrl: String?,
    val online: Int?,
    val place: String?,
    val sessionContent: String,
    val sessionEnd: List<Int>,
    val sessionIdx: Long,
    val sessionNum: Int,
    val sessionStart: List<Int>,
    var memoIdx: Long?,
    val userMemo: String?
)

data class GetAttendance(
    val nickname: String,
    var status: Int,
    val userIdx: Long
)

data class SessionInfoResponse(
    @SerializedName("result") val session: SessionInfo
)