package com.example.swith.domain.usecase.announce

import com.example.swith.domain.repository.AnnounceRepository
import com.example.swith.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class DeleteAnnounceUseCase @Inject constructor(
    private val repository: AnnounceRepository,
) {
    suspend operator fun invoke(errorEmitter: RemoteErrorEmitter, announceIdx: Long) =
        repository.deleteAnnounce(errorEmitter, announceIdx)
}