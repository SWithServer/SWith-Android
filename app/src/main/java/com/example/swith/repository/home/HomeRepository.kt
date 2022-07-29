package com.example.swith.repository.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swith.data.Group
import com.example.swith.repository.RetrofitService

class HomeRepository(private val homeRemoteDataSource: HomeRemoteDataSource){
    suspend fun getAllStudy(userId: Int) : LiveData<Group> = homeRemoteDataSource.getAllStudy(userId)
}