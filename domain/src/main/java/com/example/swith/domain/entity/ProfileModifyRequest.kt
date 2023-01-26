package com.example.swith.domain.entity

data class ProfileModifyRequest(
    val email: String,
    val interest1: Int,
    val interest2: Int,
    val introduction: String,
    val nickname: String,
    val region: String,
)