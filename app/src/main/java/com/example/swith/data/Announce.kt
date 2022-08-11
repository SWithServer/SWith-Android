package com.example.swith.data

import com.google.gson.annotations.SerializedName

data class Announce(
    val announcementContent: String,
    val announcementIdx: Int,
    val createdAt: List<Int>
)

data class AnnounceCreate(
    val announcementContent: String,
    val groupIdx: Int
)

data class AnnounceDelete(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: String
)

data class AnnounceList(
    @SerializedName("result") val announces: List<Announce>
)