package com.example.swith.repository

import com.example.swith.data.Group
import com.example.swith.data.Session
import com.example.swith.data.SessionResponse
import retrofit2.Response
import retrofit2.http.*

interface RetrofitApi {
    @GET("/groupinfo/home")
    suspend fun getAllStudy(@Query("userIdx") userIdx: Int) : Response<Group>

    @POST("/session")
    suspend fun createRound(@Body session: Session): Response<SessionResponse>
}