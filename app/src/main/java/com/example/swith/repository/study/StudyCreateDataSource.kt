package com.example.swith.repository.study

import android.util.Log
import com.example.swith.data.StudyGroup
import com.example.swith.data.StudyImageRes
import com.example.swith.data.StudyResponse
import com.example.swith.repository.RetrofitService.retrofitApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StudyCreateDataSource {

    private var groupIdx:Long = -1
    private var imgUri:String?= null

    fun postStudyInfo(studyInfo:StudyGroup) : Long {
        retrofitApi.createStudy(studyInfo).enqueue(object : Callback<StudyResponse> {
            override fun onResponse(call: Call<StudyResponse>, response: Response<StudyResponse>) {
                response.body()?.apply{
                    groupIdx = this.result.groupIdx
                }
            }

            override fun onFailure(call: Call<StudyResponse>, t: Throwable) {
                Log.e("summer", "onFailed = $t")
            }
        })
        return groupIdx
    }

    fun postStudyImage(file: File, studyInfo:StudyGroup) : String? {
        val requestFile = file.asRequestBody("image".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image",file.name,requestFile)
        retrofitApi.uploadImg(body).enqueue(object:Callback<StudyImageRes>{
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