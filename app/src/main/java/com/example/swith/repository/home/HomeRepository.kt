package com.example.swith.repository.home

import com.example.swith.repository.RetrofitApi

class HomeRepository{
    private val homeRetrofitInterface: HomeRetrofitInterface =
        RetrofitApi.retrofit.create(HomeRetrofitInterface::class.java)
    suspend fun getAllStudy() = homeRetrofitInterface.getAllStudy()
}