package com.example.swith.repository.login

import com.example.swith.remote.LoginDataSource

object LoginRepositoryProvider {
    fun provideLoginRepository() = LoginRepository(LoginDataSource())

}