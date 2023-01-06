package com.example.swith.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Round(
    val admin: Boolean,
    val attendanceValidTime: Int,
    val announcementContent: String,
    val announcementDate: List<Int>,
    var getSessionResList: List<GetSessionRes>,
    val title: String
)

data class GetSessionRes(
    val attendanceRate: Int,
    val online: Int?,
    val place: String?,
    val sessionContent: String,
    val sessionEnd: List<Int>,
    val sessionIdx: Long,
    val sessionNum: Int,
    val sessionStart: List<Int>
) : Serializable


data class RoundResponse(
    @SerializedName("result") val round: Round
)

data class GetUserAttendanceRes(
    val attendanceRate: Int,
    val nickname: String,
    val userIdx: Long
)

data class UserAttend(
    val attendanceValidTime: Int,
    val getUserAttendanceResList: List<GetUserAttendanceRes>
)

data class UserAttendResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    @SerializedName("result") val attend: UserAttend
)
