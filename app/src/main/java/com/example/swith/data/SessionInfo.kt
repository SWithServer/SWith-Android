package com.example.swith.data

import com.google.gson.annotations.SerializedName

data class SessionInfo(
    val attendanceValidTime: Int,
    var getAttendanceList: List<GetAttendance>,
    val groupImgUrl: String?,
    val online: Int?,
    val place: String?,
    val sessionContent: String,
    val sessionEnd: List<Int>,
    val sessionIdx: Int,
    val sessionNum: Int,
    val sessionStart: List<Int>,
    var memoIdx: Int?,
    val userMemo: String?
)

data class GetAttendance(
    val nickname: String,
    var status: Int,
    val userIdx: Int
)

data class SessionInfoResponse(
    @SerializedName("result") val session: SessionInfo
)