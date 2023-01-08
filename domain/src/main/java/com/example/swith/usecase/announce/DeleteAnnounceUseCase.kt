package com.example.swith.usecase.announce

import com.example.swith.repository.AnnounceRepository
import com.example.swith.utils.RemoteErrorEmitter
import javax.inject.Inject

class DeleteAnnounceUseCase @Inject constructor(
    private val repository: AnnounceRepository
) {
    suspend operator fun invoke(errorEmitter: RemoteErrorEmitter, announceIdx: Long) = repository.deleteAnnounce(errorEmitter, announceIdx)
}