package com.example.data.usecase.profile

import com.example.swith.domain.repository.ProfileRepository
import javax.inject.Inject

class GetCurrentProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,
) {
    operator fun invoke(userIdx: Long) = repository.requestCurrentProfile(userIdx)
}