package com.example.swith.repository.study

import androidx.lifecycle.LiveData
import com.example.swith.domain.entity.StudyDetailResult
import com.example.swith.domain.entity.postApplicationReq

class StudyFindDetailRepository(private val remoteDataSource:StudyFindDetailDataSource) {

    fun getStudyInfo(groupIdx:Long?): LiveData<StudyDetailResult> {
        return remoteDataSource.getStudyInfo(groupIdx)
    }

    fun postApplication(groupIdx:Long?, postApplicationReq: postApplicationReq):Int{
        return remoteDataSource.postApplication(groupIdx, postApplicationReq)
    }

}