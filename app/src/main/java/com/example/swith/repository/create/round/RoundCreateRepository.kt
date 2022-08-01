package com.example.swith.repository.create.round

import com.example.swith.data.Session


class RoundCreateRepository(private val roundCreateRemoteDataSource: RoundCreateRemoteDataSource) {
    suspend fun createRound(session: Session) : Int? = roundCreateRemoteDataSource.createRound(session)
}