package com.example.swith.domain.entity

import com.google.gson.annotations.SerializedName

data class Group(
    val announcementContent: String,
    val attendanceRate: Int,
    val groupIdx: Long,
    val groupImgUrl: String?,
    val interestContent: String,
    val memberLimit: Int,
    val online: Int,
    val regionIdx1: String,
    val regionIdx2: String,
    val sessionContent: String,
    val sessionNum: Int,
    val sessionStart: List<Int>,
    val title: String,
)

data class GroupList(
    val isSuccess: Boolean,
    val message: String,
    @SerializedName("result") val group: List<Group>,
)