package com.example.swith.domain.entity

data class LoginRequest(
    val email: String,
    val nickname: String,
    val profileImgUrl: String,
    val token: String,
)
