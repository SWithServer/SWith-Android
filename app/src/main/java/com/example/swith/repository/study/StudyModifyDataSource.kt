package com.example.swith.repository.study

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swith.data.api.RetrofitService.retrofitApi
import com.example.swith.domain.entity.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StudyModifyDataSource {
    private var groupIdx : Long = -1
    private var imgUri:String? = null
    private var studyDetailLiveData: MutableLiveData<StudyDetailResult> = MutableLiveData<StudyDetailResult>()

    fun getStudyInfo(groupIdx : Long) :MutableLiveData<StudyDetailResult>{
        retrofitApi.getStudyDetail(groupIdx).enqueue(object: Callback<StudyDetailResponse>{
            override fun onResponse(
                call: Call<StudyDetailResponse>,
                response: Response<StudyDetailResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.apply{
                        Log.e("summer","onResponse")
                        studyDetailLiveData.postValue(this.result)
                    }
                }
                else{
                    Log.e("summer", "전달실패 code = ${response.code()}")
                    Log.e("summer", "전달실패 msg = ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StudyDetailResponse>, t: Throwable) {
                Log.e("summer","onFailure = $t")
            }
        })
        return studyDetailLiveData
    }

    fun modifyStudy(groupIdx:Long, studyInfo:StudyGroup) : Long{
        retrofitApi.modifyStudy(groupIdx, studyInfo).enqueue(object: Callback<StudyModifyResponse>{
            override fun onResponse(
                call: Call<StudyModifyResponse>,
                response: Response<StudyModifyResponse>
            ) {
               if (response.isSuccessful){
                   response.body()?.apply {
                      this@StudyModifyDataSource.groupIdx = this.result
                   }
               }
                else{
                   Log.e("summer", "전달실패 code = ${response.code()}")
                   Log.e("summer", "전달실패 msg = ${response.message()}")
               }
            }

            override fun onFailure(call: Call<StudyModifyResponse>, t: Throwable) {
                Log.e("summer","onFailure t = $t")
            }
        })
        return groupIdx
    }

    fun postStudyImage(file: File):String? {
        val requestFile = file.asRequestBody("image".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image",file.name,requestFile)
        retrofitApi.uploadImg(body).enqueue(object: Callback<StudyImageRes> {
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
}