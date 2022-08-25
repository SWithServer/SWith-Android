package com.example.swith.repository.rating

import androidx.lifecycle.LiveData
import com.example.swith.data.ProfileRequest
import com.example.swith.data.RatingResponse
import com.example.swith.sourse.RatingDataSourse
import com.example.swith.sourse.UserListDataSourse


class RatingRepository(private val ratingDataSourse: RatingDataSourse, private val userListDataSourse: UserListDataSourse) {
//    fun requestCurrentRating(profileRequest: ProfileRequest): LiveData<ProfileResponse> {
//        return ratingDataSourse.requestResumeDetail()
//    }
//
//    fun getCurrentRating(): LiveData<ProfileResponse> {
//        return ratingDataSourse.
//    }

    fun requestCurrentUserList(groupIdx:String,userIdx:ProfileRequest): LiveData<RatingResponse> {
        return userListDataSourse.requestUserList(groupIdx,userIdx)
    }

    fun getCurrentUserList(): LiveData<RatingResponse> {
        return userListDataSourse.mUserListLiveData
    }
}