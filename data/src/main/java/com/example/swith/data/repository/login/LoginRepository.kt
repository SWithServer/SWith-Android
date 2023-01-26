package com.example.swith.data.repository.login

import androidx.lifecycle.LiveData
import com.example.swith.data.remote.LoginDataSource
import com.example.swith.domain.entity.LoginRequest
import com.example.swith.domain.entity.LoginResponse

class LoginRepository(private val loginDataSource: LoginDataSource) {
    fun requestCurrentLogin(loginRequest: LoginRequest): LiveData<LoginResponse> {
        return loginDataSource.requestLogin(loginRequest)
    }

    fun getCurrentLogin(): LiveData<LoginResponse> {
        return loginDataSource.mLoginLiveData
    }
}