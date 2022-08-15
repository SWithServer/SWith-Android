package com.example.swith.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swith.data.StudyGroup
import com.example.swith.data.StudyGroupResponse
import com.example.swith.data.StudyResponse
import com.example.swith.repository.RetrofitApi
import com.example.swith.repository.RetrofitService
import com.example.swith.repository.study.StudyFindRemoteDataSource
import com.example.swith.repository.study.StudyFindRepository
import com.example.swith.ui.study.create.SelectPlaceActivity
import com.example.swith.utils.base.BaseViewModel
import com.example.swith.utils.error.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudyFindViewModel : BaseViewModel() {
//    private val studyFindRepository = StudyFindRepository()
//    private val studyFind : LiveData<getStudyResponse>
//        get() = studyFindRepository._study
//
//    fun loadStudy(page:Int)
//    {
//        studyFindRepository.loadStudy(page)
//    }
//
//    fun getAll():LiveData<getStudyResponse>
//    {
//        return studyFind
//    }
}