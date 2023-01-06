package com.example.swith.repository.login

import androidx.lifecycle.LiveData
import com.example.swith.remote.LoginDataSource

class LoginRepository(private val loginDataSource: LoginDataSource) {
    fun requestCurrentLogin(loginRequest: com.example.swith.entity.LoginRequest): LiveData<com.example.swith.entity.LoginResponse> {
        return loginDataSource.requestLogin(loginRequest)
    }

    fun getCurrentLogin(): LiveData<com.example.swith.entity.LoginResponse> {
        return loginDataSource.mLoginLiveData
    }
}