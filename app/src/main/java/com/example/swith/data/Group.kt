package com.example.swith.data

import com.google.gson.annotations.SerializedName

data class GroupItem(
    val announcementContent: String,
    val attendanceRate: Int,
    val groupIdx: Int,
    val interestContent: String,
    val memberLimit: Int,
    val sessionContent: String,
    val sessionNum: Int,
    val sessionStart: List<Int>,
    val title: String
)

data class Group(
    @SerializedName("result") val group: List<GroupItem>
)