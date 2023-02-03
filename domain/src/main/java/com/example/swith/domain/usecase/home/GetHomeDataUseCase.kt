package com.example.swith.domain.usecase.home

import com.example.swith.domain.repository.HomeRepository
import javax.inject.Inject

class GetHomeDataUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(userIdx: Long) = repository.loadHomeData(userIdx)
}