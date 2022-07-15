package com.example.swith.repository.home

import com.example.swith.data.Study
import retrofit2.Response
import retrofit2.http.GET

interface HomeRetrofitInterface{
    @GET("study")
    suspend fun getAllStudy() : Response<ArrayList<Study>>
}