package com.example.swith.data

data class Memo(
    val memoContent: String,
    val sessionIdx: Int,
    val userIdx: Int
)

data class MemoUpdate(
    val memoContent: String,
    val memoIdx: Int
)

data class MemoResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: Int
)
