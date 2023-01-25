package com.example.swith.repository.study

import androidx.lifecycle.LiveData
import com.example.swith.data.StudyDetailResponse
import com.example.swith.data.StudyGroup

class StudyModifyRepository(private val remoteDataSource:StudyModifyDataSource) {

    fun getStudyInfo(groupIdx:Long) : LiveData<StudyDetailResponse> {
        return remoteDataSource.getStudyInfo(groupIdx)
    }
    fun modifyStudy(groupIdx:Long, studyInfo:StudyGroup) : Long{
        return remoteDataSource.modifyStudy(groupIdx, studyInfo)
    }
}