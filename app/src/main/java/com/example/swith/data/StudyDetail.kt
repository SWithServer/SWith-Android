package com.example.swith.data

import com.google.gson.annotations.SerializedName

data class StudyDetailResponse(
    val isSuccess: Boolean,
    val code: Long,
    val message: String,
    val result : StudyDetailResult
)

data class StudyDetailResult(
    val adminIdx: Long,
    @SerializedName("groupImgUrl") val groupImgURL : String?,
    val title: String,
    val meet: Int,
    val frequency: Int?,
    val periods: String?,
    val online: Int,
    val regionIdx1: String?,
    val regionIdx2: String?,
    val interest: Int,
    val topic: String,
    val memberLimit: Int,
    val applicationMethod: Int,
    val recruitmentEndDate: String,
    val groupStart: String,
    val groupEnd: String,
    val attendanceValidTime: Int,
    val groupContent: String,
    val numOfApplicants: Long
)
