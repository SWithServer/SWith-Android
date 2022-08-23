package com.example.swith.data

data class ProfileModifyResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: ProfileModifyResult
)
data class ProfileModifyResult(
    val email: String,
    val interestIdx1: Int,
    val interestIdx2: Int,
    val introduction: String,
    val nickname: String
)