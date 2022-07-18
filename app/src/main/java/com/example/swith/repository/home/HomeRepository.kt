package com.example.swith.repository.home

import com.example.swith.repository.RetrofitService

class HomeRepository{
    private val homeRetrofitInterface: HomeRetrofitInterface =
        RetrofitService.retrofit.create(HomeRetrofitInterface::class.java)
    suspend fun getAllStudy() = homeRetrofitInterface.getAllStudy()
}