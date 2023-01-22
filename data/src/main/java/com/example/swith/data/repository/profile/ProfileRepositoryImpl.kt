package com.example.swith.data.repository.profile

import com.example.swith.data.remote.profile.ProfileRemoteDataSource
import com.example.swith.domain.entity.ProfileResult
import com.example.swith.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileRemoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {
    override fun requestCurrentProfile(userIdx: Long): Flow<ProfileResult> {
        return profileRemoteDataSource.requestProfile(userIdx)
    }
}