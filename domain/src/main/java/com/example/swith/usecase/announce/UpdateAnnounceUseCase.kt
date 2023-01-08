package com.example.swith.usecase.announce

import com.example.swith.entity.AnnounceModify
import com.example.swith.repository.AnnounceRepository
import com.example.swith.utils.RemoteErrorEmitter
import javax.inject.Inject

class UpdateAnnounceUseCase @Inject constructor(
    private val repository: AnnounceRepository
) {
    suspend operator fun invoke(errorEmitter: RemoteErrorEmitter, announceModify: AnnounceModify)
        = repository.updateAnnounce(errorEmitter, announceModify)
}