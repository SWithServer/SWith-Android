package com.example.swith.data

data class ProfileResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: Result
)
