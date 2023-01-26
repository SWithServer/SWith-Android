package com.example.swith.data.repository.login

import com.example.swith.data.remote.LoginDataSource


object LoginRepositoryProvider {
    fun provideLoginRepository() = LoginRepository(LoginDataSource())

}