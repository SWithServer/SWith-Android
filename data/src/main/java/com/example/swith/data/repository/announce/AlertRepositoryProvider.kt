package com.example.swith.data.repository.announce

import com.example.swith.data.remote.announce.AlertDataSource


object AlertRepositoryProvider {
    fun provideAlertRepository() =
        AlertRepository(AlertDataSource())
}