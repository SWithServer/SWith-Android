package com.example.swith.repository.resume

import com.example.swith.sourse.ResumeDetailDataSourse

object ResumeDetailRepositoryProvider {
    fun resumeDetailRepository() = ResumeDetailRepository(ResumeDetailDataSourse())
}