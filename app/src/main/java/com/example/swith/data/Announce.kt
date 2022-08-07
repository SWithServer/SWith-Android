package com.example.swith.data

import com.google.gson.annotations.SerializedName

data class Announce(
    val announcementContent: String,
    val announcementIdx: Int,
    val createdAt: List<Int>
)

data class AnnounceList(
    @SerializedName("result") val announces: List<Announce>
)