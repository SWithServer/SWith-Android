package com.example.swith.data.repository.rating

import androidx.lifecycle.LiveData
import com.example.swith.data.remote.RatingDataSource
import com.example.swith.data.remote.UserListDataSource
import com.example.swith.domain.entity.RatingResponse
import com.example.swith.domain.entity.ProfileRequest


class RatingRepository(
    private val ratingDataSourse: RatingDataSource,
    private val userListDataSourse: UserListDataSource
) {
//    fun requestCurrentRating(profileRequest: ProfileRequest): LiveData<ProfileResponse> {
//        return ratingDataSourse.requestResumeDetail()
//    }
//
//    fun getCurrentRating(): LiveData<ProfileResponse> {
//        return ratingDataSourse.
//    }

    fun requestCurrentUserList(
        groupIdx: String,
        userIdx: ProfileRequest
    ): LiveData<RatingResponse> {
        return userListDataSourse.requestUserList(groupIdx, userIdx)
    }

    fun getCurrentUserList(): LiveData<RatingResponse> {
        return userListDataSourse.mUserListLiveData
    }
}