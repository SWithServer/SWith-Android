package com.example.swith.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swith.domain.entity.StudyDetailResult
import com.example.swith.domain.entity.postApplicationReq
import com.example.swith.repository.study.StudyFindDetailDataSource
import com.example.swith.repository.study.StudyFindDetailRepository

class StudyFindDetailViewModel : ViewModel() {
    private val studyDetailRepository =StudyFindDetailRepository(StudyFindDetailDataSource())

    private var _studyInfo = MutableLiveData<StudyDetailResult>()
    val studyInfo: LiveData<StudyDetailResult>
        get() = _studyInfo

    fun getStudyInfo(groupIdx:Long?){
        studyDetailRepository.getStudyInfo(groupIdx).apply{
            if (this.value != null){
                _studyInfo.value= this.value
            }
            else{
                Log.e("summer","정보 불러오기 실패")
            }
        }
    }

    fun postApplication(groupIdx:Long?, postApplicationReq: postApplicationReq):Int{
        return studyDetailRepository.postApplication(groupIdx, postApplicationReq)
    }
}