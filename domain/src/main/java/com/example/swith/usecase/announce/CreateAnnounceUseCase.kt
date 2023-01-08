package com.example.swith.usecase.announce

import com.example.swith.entity.AnnounceCreate
import com.example.swith.repository.AnnounceRepository
import com.example.swith.utils.RemoteErrorEmitter
import javax.inject.Inject

class CreateAnnounceUseCase @Inject constructor(
    private val repository: AnnounceRepository
) {
    suspend operator fun invoke(errorEmitter: RemoteErrorEmitter, announceCreate: AnnounceCreate)
        = repository.createAnnounce(errorEmitter, announceCreate)
}