package com.example.swith.entity

data class StudyFinishReq(
    val adminIdx: Long?,
    val groupIdx: Long?
)

data class StudyFinishResponse(
    val isSuccess: Boolean,
    val code: Long,
    val message: String,
    val result: Long
)
