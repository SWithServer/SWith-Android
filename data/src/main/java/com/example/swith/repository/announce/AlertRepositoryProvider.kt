package com.example.swith.repository.announce

import com.example.swith.remote.announce.AlertDataSource

object AlertRepositoryProvider {
    fun provideAlertRepository() =
        com.example.swith.repository.announce.AlertRepository(AlertDataSource())
}