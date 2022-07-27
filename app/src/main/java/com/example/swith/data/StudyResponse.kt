package com.example.swith.data

import com.google.gson.annotations.SerializedName

data class StudyResult(
    @SerializedName("groupIdx") val groupIdx:Int
)
//스터디 개설 retrofit post response data
data class StudyResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: StudyResult
)
