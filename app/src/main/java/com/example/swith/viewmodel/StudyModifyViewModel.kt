package com.example.swith.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.swith.data.StudyDetailResponse
import com.example.swith.data.StudyGroup
import com.example.swith.data.StudyImageRes
import com.example.swith.repository.RetrofitService
import com.example.swith.repository.study.StudyModifyDataSource
import com.example.swith.repository.study.StudyModifyRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StudyModifyViewModel : ViewModel() {
    private val studyModifyRepository = StudyModifyRepository(StudyModifyDataSource())
    private var imgUri:String? = null

    fun getStudyInfo(groupIdx:Long) : LiveData<StudyDetailResponse> {
        return studyModifyRepository.getStudyInfo(groupIdx)
    }

    fun modifyStudy(groupIdx:Long, studyInfo: StudyGroup) : Long{
        return studyModifyRepository.modifyStudy(groupIdx, studyInfo)
    }

    fun postStudyImage(file: File):String? {
        val requestFile = file.asRequestBody("image".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image",file.name,requestFile)
        RetrofitService.retrofitApi.uploadImg(body).enqueue(object: Callback<StudyImageRes> {
            override fun onResponse(call: Call<StudyImageRes>, response: Response<StudyImageRes>) {
                if(response.isSuccessful){
                    Log.e("summer", "성공$response")
                    response.body()?.apply {
                        Log.e("summer 결과값", this.imageUrls[0])
                        imgUri = this.imageUrls[0]
                    }
                }
                else {
                    Log.e("summer", "전달실패 code = ${response.code()}")
                    Log.e("summer", "전달실패 msg = ${response.message()}")
                }
            }
            override fun onFailure(call: Call<StudyImageRes>, t: Throwable) {
                Log.e("summer", "onFailure t = ${t.toString()}")
                Log.e("summer", "onFailure msg = ${t.message}")
            }
        })
        return imgUri
    }

    class Factory : ViewModelProvider.NewInstanceFactory() {

        //@Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileModifyViewModel() as T
        }
    }
}