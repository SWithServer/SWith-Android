package com.example.swith.sourse

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swith.data.ProfileRequest
import com.example.swith.data.ProfileResponse
import com.example.swith.repository.RetrofitService.retrofitApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileDataSourse {
    internal val mProfileLiveData: MutableLiveData<ProfileResponse> = MutableLiveData<ProfileResponse>()

    fun requestProfile(profileRequest: ProfileRequest) : LiveData<ProfileResponse> {
        retrofitApi.getProfileInfo(profileRequest).enqueue(object :Callback<ProfileResponse>{
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                Log.e("doori","onResponse = $response")
                response.body()?.apply {
                    Log.e("doori","response body = ${response.body()}")
                    mProfileLiveData.postValue(this)
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Log.e("doori", "onFailed = $t")
            }

        })

        return mProfileLiveData
    }
}