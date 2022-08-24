package com.example.swith.sourse

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swith.data.ProfileModifyResponse
import com.example.swith.data.ProfileResponse
import com.example.swith.data.ResumeResponse
import com.example.swith.repository.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResumeDataSourse {
    internal val mResumeData: MutableLiveData<ResumeResponse> = MutableLiveData<ResumeResponse>()

    fun requestResume(userIdx:Long):LiveData<ResumeResponse>{
        RetrofitService.retrofitApi.getResume(userIdx).enqueue(object : Callback<ResumeResponse> {
            override fun onResponse(call: Call<ResumeResponse>, response: Response<ResumeResponse>) {
                Log.e("doori","onResponse = $response")
                response.body().apply {
                    Log.e("doori","response body = ${response.body()}")
                    mResumeData.postValue(this)
                }
            }

            override fun onFailure(call: Call<ResumeResponse>, t: Throwable) {
                Log.e("doori", "onFailed = $t")
            }

        })

        return mResumeData
    }
}