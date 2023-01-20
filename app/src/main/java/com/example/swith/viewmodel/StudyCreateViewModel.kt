package com.example.swith.viewmodel

import androidx.lifecycle.ViewModel
import com.example.swith.data.StudyGroup
import com.example.swith.repository.study.StudyCreateDataSource
import com.example.swith.repository.study.StudyCreateRepository

class StudyCreateViewModel : ViewModel()  {

    private val studyCreateRepository = StudyCreateRepository(StudyCreateDataSource())

    fun postStudyInfo(studyInfo:StudyGroup):Long{
        return studyCreateRepository.postStudy(studyInfo)
    }
}