package com.example.swith.data

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
    val group: List<GroupItem>
)