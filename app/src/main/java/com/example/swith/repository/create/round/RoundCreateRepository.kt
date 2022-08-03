package com.example.swith.repository.create.round

import com.example.swith.data.Session
import com.example.swith.data.SessionResponse


class RoundCreateRepository(private val roundCreateRemoteDataSource: RoundCreateRemoteDataSource) {
    suspend fun createRound(session: Session) : SessionResponse? = roundCreateRemoteDataSource.createRound(session)
}