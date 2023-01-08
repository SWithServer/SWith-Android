package com.example.swith.usecase.announce

import com.example.swith.repository.AnnounceRepository
import com.example.swith.utils.RemoteErrorEmitter
import javax.inject.Inject

class GetAnnounceDataUseCase @Inject constructor(
    private val repository: AnnounceRepository
) {
    suspend operator fun invoke(errorEmitter: RemoteErrorEmitter, groupIdx: Long) = repository.getAllAnnounce(errorEmitter, groupIdx)
}