package com.example.swith.repository.rating


import com.example.swith.remote.RatingDataSource
import com.example.swith.remote.UserListDataSource

object RatingRepositoryProvider {
    fun provideRatingRepository() = RatingRepository(RatingDataSource(), UserListDataSource())
}