package com.example.swith.data

import com.google.gson.annotations.SerializedName

data class GroupItem(
    val groupIdx: Int,
    val title: String,
    val memberLimit: Int,
    val interestContent: String,

    val announcementContent: String,
    val sessionNum: Int,
    val sessionContent: String,
    val sessionStart: List<Int>,
    val attendanceRate: Int
)

data class Group(
    @SerializedName("result") val group: List<GroupItem>
)