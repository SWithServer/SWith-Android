package com.example.swith.entity

import okhttp3.MultipartBody

data class StudyImageRes(
    val imageUrls: List<String>
)

data class StudyImageReq(
    val image: MultipartBody.Part
)

