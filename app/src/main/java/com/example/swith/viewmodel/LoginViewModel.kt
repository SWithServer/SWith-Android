package com.example.swith.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.swith.data.LoginRequest
import com.example.swith.data.LoginResponse
import com.example.swith.repository.login.LoginRepository
import com.example.swith.repository.login.LoginRepositoryProvider

class LoginViewModel:ViewModel() {
    val isLoading: ObservableField<Boolean> = ObservableField<Boolean>()
    private val mLoginRepository: LoginRepository = LoginRepositoryProvider.provideLoginRepository()
    init {
        hideLoading()
    }

    fun showLoading() {
        isLoading.set(true)
    }

    fun hideLoading() {
        isLoading.set(false)
    }

    fun requestCurrentLogin(loginRequest: LoginRequest): LiveData<LoginResponse> {
        return mLoginRepository.requestCurrentLogin(loginRequest)
    }

    fun getCurrentLogin(): LiveData<LoginResponse> {
        return mLoginRepository.getCurrentLogin()
    }

    class Factory : ViewModelProvider.NewInstanceFactory() {

        //@Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel() as T
        }
    }
}