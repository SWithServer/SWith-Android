package com.example.swith.repository.resume

import com.example.swith.sourse.ResumeDataSourse

object ResumeRepositoryProvider {
    fun resumeRepository() = ResumeRepository(ResumeDataSourse())
}