package com.example.swith.repository.profile

import androidx.lifecycle.LiveData
import com.example.swith.data.ProfileModifyRequest
import com.example.swith.data.ProfileModifyResponse
import com.example.swith.data.ProfileRequest
import com.example.swith.data.ProfileResponse
import com.example.swith.sourse.ProfileDataSourse
import com.example.swith.sourse.ProfileModifyDataSourse

class ProfileModifyRepository(private val profileDataSource: ProfileDataSourse,private val profileModifyDataSourse: ProfileModifyDataSourse) {
    fun requestCurrentProfile(profileRequest: ProfileRequest): LiveData<ProfileResponse> {
        return profileDataSource.requestProfile(profileRequest)
    }

    fun getCurrentProfile(): LiveData<ProfileResponse> {
        return profileDataSource.mProfileLiveData
    }

    fun requestCurrentProfileModify(profileModifyRequest: ProfileModifyRequest): LiveData<ProfileModifyResponse> {
        return profileModifyDataSourse.requestProfileModify(profileModifyRequest)
    }

    fun getCurrentProfileModify(): LiveData<ProfileModifyResponse> {
        return profileModifyDataSourse.mProfileModifyLiveData
    }
}