package com.example.swith.data.repository.resume

import com.example.swith.data.remote.ResumeDataSource

object ResumeRepositoryProvider {
    fun resumeRepository() = ResumeRepository(ResumeDataSource())
}