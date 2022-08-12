package com.example.swith.data

import com.google.gson.annotations.SerializedName

data class Group(
    val announcementContent: String,
    val attendanceRate: Int,
    val groupIdx: Int,
    val groupImageUrl: String?,
    val interestContent: String,
    val memberLimit: Int,
    val online: Int,
    val regionIdx1: Long,
    val regionIdx2: Long,
    val sessionContent: String,
    val sessionNum: Int,
    val sessionStart: List<Int>,
    val title: String,
)

data class GroupList(
    val isSuccess: Boolean,
    val message: String,
    @SerializedName("result") val group: List<Group>
)

data class GroupRV(
    val announcementContent: String,
    val attendanceRate: Int,
    val groupIdx: Int,
    val groupImageUrl: String?,
    val interestContent: String,
    val memberLimit: Int,
    val online: Int,
    val place: String?,
    val sessionContent: String,
    val sessionNum: Int,
    val sessionStart: List<Int>,
    val title: String,
)