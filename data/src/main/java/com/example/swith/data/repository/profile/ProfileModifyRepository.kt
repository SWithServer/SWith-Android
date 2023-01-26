package com.example.swith.data.repository.profile

import androidx.lifecycle.LiveData
import com.example.swith.data.remote.ProfileModifyDataSource
import com.example.swith.data.remote.profile.ProfileRemoteDataSource
import com.example.swith.domain.entity.ProfileModifyRequest
import com.example.swith.domain.entity.ProfileModifyResponse

class ProfileModifyRepository(
    private val profileDataSource: ProfileRemoteDataSource,
    private val profileModifyDataSourse: ProfileModifyDataSource
) {
//    fun requestCurrentProfile(profileRequest: com.example.swith.entity.ProfileRequest): LiveData<ProfileResponse> {
////        return profileDataSource.requestProfile(profileRequest)
//    }
//
//    fun getCurrentProfile(): LiveData<ProfileResponse> {
////        return profileDataSource.mProfileLiveData
//    }

    fun requestCurrentProfileModify(profileModifyRequest: ProfileModifyRequest): LiveData<ProfileModifyResponse> {
        return profileModifyDataSourse.requestProfileModify(profileModifyRequest)
    }

    fun getCurrentProfileModify(): LiveData<ProfileModifyResponse> {
        return profileModifyDataSourse.mProfileModifyLiveData
    }
}