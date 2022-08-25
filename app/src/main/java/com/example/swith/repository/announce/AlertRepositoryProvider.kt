package com.example.swith.repository.announce

import com.example.swith.repository.login.LoginRepository
import com.example.swith.sourse.AlertDataSourse
import com.example.swith.sourse.LoginDataSourse

object AlertRepositoryProvider {
    fun provideAlertRepository() = AlertRepository(AlertDataSourse())
}