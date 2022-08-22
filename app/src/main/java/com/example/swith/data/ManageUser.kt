package com.example.swith.data

// 스터디 가입 방법이 지원인지 선착인지 보내주면 그 스터디에 해당하는
// 유저들의 목록을 보내주는 data class
data class ManageUserResponse(
    val isSuccess: Boolean,
    val code: Long,
    val message: String,
    val result: List<ManageUserResult>
)

data class ManageUserResult(
    val userIdx: Long,
    val nickname: String,
    val profileImgUrl: String,
    val applicationIdx: Long,
    val applicationContent: String?
)

data class ManageUserProfileResponse(
    val isSuccess: Boolean,
    val code: Long,
    val message: String,
    val result: ManageUserProfileResult
)

data class ManageUserProfileResult(
    val userIdx: Long,
    val email: String,
    val nickname: String,
    val profileImgUrl: String,
    val introduction: String,
    val interestIdx1: Long,
    val interestIdx2: Long,
    val averageStar: Double,
    val role: String,
    val accessToken: Any? = null,
    val refreshToken: String,
    val status: Long
)

data class ManageUserIdx(
    val userIdx: Long?
)

