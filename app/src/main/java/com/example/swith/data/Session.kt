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
