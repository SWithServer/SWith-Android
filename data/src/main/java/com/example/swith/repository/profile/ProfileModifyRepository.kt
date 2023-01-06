package com.example.swith.repository.profile

import androidx.lifecycle.LiveData
import com.example.swith.entity.ProfileResponse
import com.example.swith.remote.ProfileDataSource
import com.example.swith.remote.ProfileModifyDataSource

class ProfileModifyRepository(
    private val profileDataSource: ProfileDataSource,
    private val profileModifyDataSourse: ProfileModifyDataSource
) {
    fun requestCurrentProfile(profileRequest: com.example.swith.entity.ProfileRequest): LiveData<ProfileResponse> {
        return profileDataSource.requestProfile(profileRequest)
    }

    fun getCurrentProfile(): LiveData<ProfileResponse> {
        return profileDataSource.mProfileLiveData
    }

    fun requestCurrentProfileModify(profileModifyRequest: com.example.swith.entity.ProfileModifyRequest): LiveData<com.example.swith.entity.ProfileModifyResponse> {
        return profileModifyDataSourse.requestProfileModify(profileModifyRequest)
    }

    fun getCurrentProfileModify(): LiveData<com.example.swith.entity.ProfileModifyResponse> {
        return profileModifyDataSourse.mProfileModifyLiveData
    }
}