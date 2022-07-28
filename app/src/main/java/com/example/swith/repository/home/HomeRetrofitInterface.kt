package com.example.swith.repository.home

import com.example.swith.data.Group
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeRetrofitInterface{
    @GET("/groupinfo/{id}")
    suspend fun getAllStudy(@Path("id") userId: Int) : Response<Group>
}