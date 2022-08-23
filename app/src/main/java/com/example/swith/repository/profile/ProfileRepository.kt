package com.example.swith.repository.profile

import androidx.lifecycle.LiveData
import com.example.swith.data.ProfileRequest
import com.example.swith.data.ProfileResponse
import com.example.swith.sourse.ProfileDataSourse

class ProfileRepository(private val profileDataSource: ProfileDataSourse)  {
   fun requestCurrentProfile(profileRequest: ProfileRequest): LiveData<ProfileResponse> {
        return profileDataSource.requestProfile(profileRequest)
    }

    fun getCurrentProfile(): LiveData<ProfileResponse> {
        return profileDataSource.mProfileLiveData
    }
}