package com.example.swith.domain.repository

import com.example.swith.domain.entity.ProfileResult
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun requestCurrentProfile(userIdx: Long): Flow<ProfileResult>
}