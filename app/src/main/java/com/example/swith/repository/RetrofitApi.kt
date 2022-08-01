package com.example.swith.repository

import com.example.swith.data.Group
import com.example.swith.data.Session
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitApi {
    @GET("/groupinfo/{id}")
    suspend fun getAllStudy(@Path("id") userId: Int) : Response<Group>

    @POST("/session")
    suspend fun createRound(@Body session: Session): Response<Int>
}