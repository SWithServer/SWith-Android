package com.example.swith.data.remote.profile


import com.example.swith.data.api.SwithService
import com.example.swith.domain.entity.ProfileRequest
import com.example.swith.domain.entity.ProfileResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProfileRemoteDataSource @Inject constructor(
    private val swithService: SwithService
){
    fun requestProfile(
        userIdx: Long
    ) : Flow<ProfileResult> = flow {
        emit(swithService.getProfileInfo(ProfileRequest(userIdx)).result)
    }
}