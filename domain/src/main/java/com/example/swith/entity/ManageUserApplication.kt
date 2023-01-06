package com.example.swith.entity

data class ManageUserResumeResponse(
    val isSuccess: Boolean,
    val code: Long,
    val message: String,
    val result: ManageUserResumeResult
)

data class ManageUserResumeResult(
    val applicationIdx: Long,
    val status: Long
)

data class ManageUserResumeReq(
    val applicationIdx: Long?,
    val adminIdx: Long?,
    val statusOfApplication: Int
)