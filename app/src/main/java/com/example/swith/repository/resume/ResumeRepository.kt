package com.example.swith.repository.resume

import androidx.lifecycle.LiveData
import com.example.swith.data.ProfileRequest
import com.example.swith.data.ProfileResponse
import com.example.swith.data.ResumeResponse
import com.example.swith.sourse.ResumeDataSourse

class ResumeRepository(private val resumeDataSource: ResumeDataSourse) {
    fun requestCurrentResume(userIdx: Int): LiveData<ResumeResponse> {
        return resumeDataSource.requestResume(userIdx)
    }

    fun getCurrentResume(): LiveData<ResumeResponse> {
        return resumeDataSource.mResumeData
    }
}