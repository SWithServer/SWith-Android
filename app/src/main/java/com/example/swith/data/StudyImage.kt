package com.example.swith.data

import okhttp3.MultipartBody
import java.io.File

data class StudyImageRes(
    val imageUrls : List<String>
)

data class StudyImageReq(
    val image : MultipartBody.Part
)

