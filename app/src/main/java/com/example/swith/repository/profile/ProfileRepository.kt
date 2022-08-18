package com.example.swith.repository.profile

import androidx.lifecycle.LiveData
import com.example.swith.data.ProfileResponse
import com.example.swith.sourse.ProfileDataSourse

class ProfileRepository(private val profileDataSource: ProfileDataSourse)  {
   fun requestCurrentProfile(email: String, jwt:String): LiveData<ProfileResponse> {
        return profileDataSource.requestProfile(email,jwt)
    }

    fun getCurrentProfile(): LiveData<ProfileResponse> {
        return profileDataSource.mProfileLiveData
    }
}