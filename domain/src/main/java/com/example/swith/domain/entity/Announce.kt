package com.example.swith.domain.entity

import com.google.gson.annotations.SerializedName

data class Announce(
    val announcementContent: String,
    val announcementIdx: Long,
    val createdAt: List<Int>,
)

data class AnnounceCreate(
    val announcementContent: String,
    val groupIdx: Long,
)

data class AnnounceDelete(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: String,
)

data class AnnounceModify(
    val announcementContent: String,
    val announcementIdx: Long,
)

data class AnnounceList(
    @SerializedName("result") val announces: List<Announce>,
)