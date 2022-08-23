package com.example.swith.sourse

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swith.data.ProfileModifyRequest
import com.example.swith.data.ProfileModifyResponse
import com.example.swith.data.ProfileRequest
import com.example.swith.data.ProfileResponse
import com.example.swith.repository.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileModifyDataSourse {
    internal val mProfileModifyLiveData: MutableLiveData<ProfileModifyResponse> = MutableLiveData<ProfileModifyResponse>()
    fun requestProfileModify(profileModifyRequest: ProfileModifyRequest) : LiveData<ProfileModifyResponse> {
        RetrofitService.retrofitApi.setProfile(profileModifyRequest).enqueue(object : Callback<ProfileModifyResponse> {
            override fun onResponse(call: Call<ProfileModifyResponse>, response: Response<ProfileModifyResponse>) {
                Log.e("doori","onResponse = $response")
                response.body().apply {
                    Log.e("doori","response body = ${response.body()}")
                    mProfileModifyLiveData.postValue(this)
                }
            }

            override fun onFailure(call: Call<ProfileModifyResponse>, t: Throwable) {
                Log.e("doori", "onFailed = $t")
            }

        })

        return mProfileModifyLiveData
    }
}