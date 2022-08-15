package com.example.swith.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Round(
    val admin: Boolean,
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
    val sessionIdx: Int,
    val sessionNum: Int,
    val sessionStart: List<Int>
): Serializable

data class RoundResponse(
    @SerializedName("result") val round: Round
)
