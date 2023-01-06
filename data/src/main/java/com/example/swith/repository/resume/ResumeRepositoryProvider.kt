package com.example.swith.repository.resume

import com.example.swith.remote.ResumeDataSource

object ResumeRepositoryProvider {
    fun resumeRepository() = ResumeRepository(ResumeDataSource())
}