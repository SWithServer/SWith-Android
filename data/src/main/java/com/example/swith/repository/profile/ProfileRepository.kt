package com.example.swith.repository.profile

import androidx.lifecycle.LiveData
import com.example.swith.entity.ProfileResponse
import com.example.swith.remote.ProfileDataSource

class ProfileRepository(private val profileDataSource: ProfileDataSource) {
    fun requestCurrentProfile(profileRequest: com.example.swith.entity.ProfileRequest): LiveData<ProfileResponse> {
        return profileDataSource.requestProfile(profileRequest)
    }

    fun getCurrentProfile(): LiveData<ProfileResponse> {
        return profileDataSource.mProfileLiveData
    }
}