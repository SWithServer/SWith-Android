package com.example.swith.data

data class Session(
    val groupIdx: Int,
    val online: Int,
    val place: String,
    val sessionContent: String,
    val sessionEnd: String,
    val sessionStart: String,
    val userIdx: Int
)

data class SessionCreate(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: Int?
)

data class SessionResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: String
)
