package com.example.swith.data

data class StudyFinishReq(
    val adminIdx: Long?,
    val groupIdx:Long?
)

data class StudyFinishResponse(
    val isSuccess: Boolean,
    val code: Long,
    val message: String,
    val result: Long
)
