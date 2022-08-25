package com.example.swith.repository.rating


import com.example.swith.sourse.RatingDataSourse
import com.example.swith.sourse.UserListDataSourse

object RatingRepositoryProvider {
    fun provideRatingRepository() = RatingRepository(RatingDataSourse(), UserListDataSourse())
}