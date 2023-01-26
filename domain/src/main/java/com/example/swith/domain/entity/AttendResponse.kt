package com.example.swith.domain.entity

data class AttendResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: Int,
)