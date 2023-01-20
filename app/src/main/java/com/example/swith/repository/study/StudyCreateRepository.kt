package com.example.swith.repository.study

import com.example.swith.data.StudyGroup

class StudyCreateRepository(private val remoteDataSource:StudyCreateDataSource) {

    fun postStudy(studyInfo:StudyGroup): Long {
        return remoteDataSource.postStudyInfo(studyInfo)
    }

}