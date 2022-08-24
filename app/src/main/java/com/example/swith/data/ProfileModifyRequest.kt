package com.example.swith.data

data class ProfileModifyRequest(
    val email: String,
    val interest1: Int,
    val interest2: Int,
    val introduction: String,
    val nickname: String,
    val region: String
)