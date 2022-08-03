package com.example.swith.data

import com.google.gson.annotations.SerializedName

data class Session(
    @SerializedName("groupIdx") val groupIdx: Int,
    @SerializedName("online") val online: Int,
    @SerializedName("place") val place: String,
    @SerializedName("sessionContent") val sessionContent: String,
    @SerializedName("sessionEnd") val sessionEnd: String,
    @SerializedName("sessionStart") val sessionStart: String,
    @SerializedName("userIdx") val userIdx: Int
)

data class SessionResponse(
    @SerializedName("result") val result: Int?
)
