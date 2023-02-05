package com.example.swith.repository.study

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.swith.R
import com.example.swith.data.api.RetrofitService.retrofitApi
import com.example.swith.domain.entity.StudyApplicationResponse
import com.example.swith.domain.entity.StudyDetailResponse
import com.example.swith.domain.entity.StudyDetailResult
import com.example.swith.domain.entity.postApplicationReq
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudyFindDetailDataSource {

    private var status = -1
    private var _studyInfo = MutableLiveData<StudyDetailResult>().apply{
        value=null
    }

    private val studyInfo : LiveData<StudyDetailResult>
        get() = _studyInfo

    fun getStudyInfo(groupIdx:Long?) : LiveData<StudyDetailResult>{
        retrofitApi.getStudyDetail(groupIdx!!).enqueue(object : Callback<StudyDetailResponse> {
            override fun onResponse(
                call: Call<StudyDetailResponse>,
                response: Response<StudyDetailResponse>,
            ) {
                if (response.isSuccessful) {
                    Log.e("summer", "성공$response")
                    response.body()?.apply {
                        Log.e("스터디 정보 값들", this.result.toString())
                    }
                } else {
                    Log.e("summer", "전달실패 code = ${response.code()}")
                    Log.e("summer", "전달실패 msg = ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StudyDetailResponse>, t: Throwable) {
                Log.e("summer", "onFailure t = $t")
                Log.e("summer", "onFailure msg = ${t.message}")
            }
        })
        return studyInfo
    }

    fun postApplication(groupIdx:Long?, postApplicationReq: postApplicationReq) : Int {
        retrofitApi.postApplication(groupIdx!!, postApplicationReq)
            .enqueue(object : Callback<StudyApplicationResponse> {
                override fun onResponse(
                    call: Call<StudyApplicationResponse>,
                    response: Response<StudyApplicationResponse>,
                ) {
                    if (response.isSuccessful) {
                        Log.e("summer", "성공${response.toString()}")
                        response.body()?.apply {
                            Log.e("post data =", "${groupIdx},${postApplicationReq}")
                        }
                    } else {
                        Log.e("summer", "전달실패 code = ${response.code()}")
                        Log.e("summer", "전달실패 msg = ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<StudyApplicationResponse>, t: Throwable) {
                    Log.e("summer", "onFailure t = ${t.toString()}")
                    Log.e("summer", "onFailure msg = ${t.message}")
                }
            })
            return status
    }
}