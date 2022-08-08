package com.example.swith.viewmodel

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.example.swith.repository.study.StudyFindRemoteDataSource
import com.example.swith.repository.study.StudyFindRepository
import com.example.swith.ui.study.create.SelectPlaceActivity

class StudyFindViewModel : ViewModel() {
    private val repository : StudyFindRepository = StudyFindRepository(StudyFindRemoteDataSource())
    //스터디 정보 repository로 받아오기
    init{
    }
}