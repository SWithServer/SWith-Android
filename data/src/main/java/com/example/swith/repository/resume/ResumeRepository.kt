package com.example.swith.repository.resume

import androidx.lifecycle.LiveData
import com.example.swith.entity.ResumeResponse
import com.example.swith.remote.ResumeDataSource

class ResumeRepository(private val resumeDataSource: ResumeDataSource) {
    fun requestCurrentResume(userIdx: Long): LiveData<ResumeResponse> {
        return resumeDataSource.requestResume(userIdx)
    }

    fun getCurrentResume(): LiveData<ResumeResponse> {
        return resumeDataSource.mResumeData
    }
}