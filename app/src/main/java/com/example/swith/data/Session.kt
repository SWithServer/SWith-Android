package com.example.swith.data

import com.google.gson.annotations.SerializedName

data class Session(
    val groupIdx: Int,
    val online: Int,
    val place: String,
    val sessionContent: String,
    val sessionEnd: String,
    val sessionStart: String,
    val userIdx: Int
)

data class SessionResponse(
    val result: Int?
)
