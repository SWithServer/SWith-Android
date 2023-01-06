package com.example.swith.entity

data class LoginRequest(
    val email: String,
    val nickname: String,
    val profileImgUrl: String,
    val token: String
)
