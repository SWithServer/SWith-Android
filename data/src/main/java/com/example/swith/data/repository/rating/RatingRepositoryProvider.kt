package com.example.swith.data.repository.rating

import com.example.swith.data.remote.RatingDataSource
import com.example.swith.data.remote.UserListDataSource


object RatingRepositoryProvider {
    fun provideRatingRepository() = RatingRepository(RatingDataSource(), UserListDataSource())
}