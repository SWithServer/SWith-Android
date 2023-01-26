package com.example.swith.data.repository.resume

import androidx.lifecycle.LiveData
import com.example.swith.data.remote.ResumeDataSource
import com.example.swith.domain.entity.ResumeResponse

class ResumeRepository(private val resumeDataSource: ResumeDataSource) {
    fun requestCurrentResume(userIdx: Long): LiveData<ResumeResponse> {
        return resumeDataSource.requestResume(userIdx)
    }

    fun getCurrentResume(): LiveData<ResumeResponse> {
        return resumeDataSource.mResumeData
    }
}