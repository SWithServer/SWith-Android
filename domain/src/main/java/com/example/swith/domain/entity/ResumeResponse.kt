package com.example.swith.domain.entity

data class ResumeResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<ResumeResult>,
)

data class ResumeResult(
    val applicationContent: String,
    val applicationIdx: Int,
    val createdAt: List<Int>,
    val online: Int,
    val regionIdx1: String,
    val regionIdx2: String,
    val status: Int,
    val title: String,
)
