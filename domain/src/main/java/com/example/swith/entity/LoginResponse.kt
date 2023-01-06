package com.example.swith.entity

data class LoginResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: LoginResult
)

data class LoginResult(
    val accessToken: String,
    val averageStar: Any,
    val email: String,
    val interestIdx1: Any,
    val interestIdx2: Any,
    val introduction: Any,
    val isSignUp: Boolean,
    val nickname: String,
    val profileImgUrl: String,
    val refreshToken: String,
    val noticeToken: String,
    val role: String,
    val status: Int,
    val userIdx: Long
)