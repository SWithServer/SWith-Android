package com.example.swith.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swith.repository.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileModifyDataSource {
    internal val mProfileModifyLiveData: MutableLiveData<com.example.swith.entity.ProfileModifyResponse> =
        MutableLiveData<com.example.swith.entity.ProfileModifyResponse>()

    fun requestProfileModify(profileModifyRequest: com.example.swith.entity.ProfileModifyRequest): LiveData<com.example.swith.entity.ProfileModifyResponse> {
        RetrofitService.retrofitApi.setProfile(profileModifyRequest)
            .enqueue(object : Callback<com.example.swith.entity.ProfileModifyResponse> {
                override fun onResponse(
                    call: Call<com.example.swith.entity.ProfileModifyResponse>,
                    response: Response<com.example.swith.entity.ProfileModifyResponse>
                ) {
                    Log.e("doori", "onResponse = $response")
                    response.body().apply {
                        Log.e("doori", "response body = ${response.body()}")
                        mProfileModifyLiveData.postValue(this)
                    }
                }

                override fun onFailure(
                    call: Call<com.example.swith.entity.ProfileModifyResponse>,
                    t: Throwable
                ) {
                    Log.e("doori", "onFailed = $t")
                }

            })

        return mProfileModifyLiveData
    }
}