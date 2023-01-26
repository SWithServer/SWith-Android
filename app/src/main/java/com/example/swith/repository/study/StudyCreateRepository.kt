package com.example.swith.repository.study

import com.example.swith.domain.entity.StudyGroup
import java.io.File

class StudyCreateRepository(private val remoteDataSource:StudyCreateDataSource) {

    fun postStudy(studyInfo: StudyGroup): Long {
        return remoteDataSource.postStudyInfo(studyInfo)
    }

    fun postImage(file: File, studyInfo:StudyGroup): String?{
        return remoteDataSource.postStudyImage(file,studyInfo)
    }

}