package com.example.swith.repository.rating

import androidx.lifecycle.LiveData
import com.example.swith.entity.RatingResponse
import com.example.swith.remote.RatingDataSource
import com.example.swith.remote.UserListDataSource


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
        userIdx: com.example.swith.entity.ProfileRequest
    ): LiveData<RatingResponse> {
        return userListDataSourse.requestUserList(groupIdx, userIdx)
    }

    fun getCurrentUserList(): LiveData<RatingResponse> {
        return userListDataSourse.mUserListLiveData
    }
}