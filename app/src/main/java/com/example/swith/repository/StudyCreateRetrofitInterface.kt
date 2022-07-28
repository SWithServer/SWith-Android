package com.example.swith.repository

import com.example.swith.data.StudyGroup
import com.example.swith.data.StudyResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface StudyCreateRetrofitInterface {
    @POST("/groupinfo")
    fun createStudy(@Body body:StudyGroup) : Call<StudyResponse>
}