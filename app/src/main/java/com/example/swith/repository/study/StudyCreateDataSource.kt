package com.example.swith.repository.study

import android.util.Log
import com.example.swith.data.StudyGroup
import com.example.swith.data.StudyResponse
import com.example.swith.repository.RetrofitService.retrofitApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudyCreateDataSource {

    private var groupIdx:Long = -1

    fun postStudyInfo(studyInfo:StudyGroup) : Long{
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

}