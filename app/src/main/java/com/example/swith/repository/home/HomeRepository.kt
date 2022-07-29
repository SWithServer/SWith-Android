package com.example.swith.repository.home

import com.example.swith.data.Group
import com.example.swith.repository.RetrofitService

class HomeRepository{
    private val retrofitService: HomeRetrofitInterface =
        RetrofitService.retrofit.create(HomeRetrofitInterface::class.java)

    suspend fun getAllStudy(userId: Int) : Group? {
        val response = retrofitService.getAllStudy(userId)
        if (response.isSuccessful){
            return response.body()
        }
        return null
    }
}