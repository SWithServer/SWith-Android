package com.example.swith.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.swith.data.api.RetrofitService.retrofitApi
import com.example.swith.domain.entity.*
import com.example.swith.repository.study.StudyModifyDataSource
import com.example.swith.repository.study.StudyModifyRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StudyModifyViewModel : ViewModel() {
    private val studyModifyRepository = StudyModifyRepository(StudyModifyDataSource())

    private var _studyLiveData = MutableLiveData<StudyDetailResult>()
    private var _loadingView = MutableLiveData<Boolean>()

    val studyLiveData:LiveData<StudyDetailResult>
        get() = _studyLiveData

    val loadingView :LiveData<Boolean>
        get() = _loadingView

    fun getStudyInfo(groupIdx:Long): StudyDetailResult? {
            _studyLiveData.value= studyModifyRepository.getStudyInfo(groupIdx).value
            _loadingView.value=false
        return studyLiveData.value
    }

    fun modifyStudy(groupIdx:Long, studyInfo: StudyGroup) : Long{
        return studyModifyRepository.modifyStudy(groupIdx, studyInfo)
    }

    fun postStudyImage(file: File):String? {
        return studyModifyRepository.postStudyImage(file)
    }

    fun getChecked(value:Int): Boolean{
        return value==1
    }
}