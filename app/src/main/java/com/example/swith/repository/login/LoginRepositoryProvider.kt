package com.example.swith.repository.login

import com.example.swith.repository.profile.ProfileRepository
import com.example.swith.sourse.LoginDataSourse
import com.example.swith.sourse.ProfileDataSourse

object LoginRepositoryProvider {
    fun provideLoginRepository() = LoginRepository(LoginDataSourse())

}