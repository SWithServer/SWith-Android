package com.example.swith.entity

data class Memo(
    val memoContent: String,
    val sessionIdx: Long,
    val userIdx: Long
)

data class MemoUpdate(
    val memoContent: String,
    val memoIdx: Long
)

data class MemoResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: Long
)
