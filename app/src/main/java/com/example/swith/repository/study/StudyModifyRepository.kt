package com.example.swith.repository.study

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swith.domain.entity.StudyDetailResponse
import com.example.swith.domain.entity.StudyDetailResult
import com.example.swith.domain.entity.StudyGroup
import java.io.File

class StudyModifyRepository(private val remoteDataSource:StudyModifyDataSource) {

    fun getStudyInfo(groupIdx:Long) : MutableLiveData<StudyDetailResult> {
        return remoteDataSource.getStudyInfo(groupIdx)
    }
    fun modifyStudy(groupIdx:Long, studyInfo:StudyGroup) : Long{
        return remoteDataSource.modifyStudy(groupIdx, studyInfo)
    }
    fun postStudyImage(file: File):String?{
        return remoteDataSource.postStudyImage(file)
    }
}