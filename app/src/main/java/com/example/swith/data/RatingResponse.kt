package com.example.swith.data

data class RatingResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<RatingResult>
)
data class RatingResult(
    val nickname: String,
    val userIdx: Long
)