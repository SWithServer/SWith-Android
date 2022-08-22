package com.example.swith.repository.login

import androidx.lifecycle.LiveData
import com.example.swith.data.LoginRequest
import com.example.swith.data.LoginResponse
import com.example.swith.sourse.LoginDataSourse

class LoginRepository(private val loginDataSource: LoginDataSourse) {
    fun requestCurrentLogin(loginRequest: LoginRequest): LiveData<LoginResponse> {
        return loginDataSource.requestLogin(loginRequest)
    }

    fun getCurrentLogin(): LiveData<LoginResponse> {
        return loginDataSource.mLoginLiveData
    }
}