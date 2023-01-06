package com.example.swith.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swith.repository.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginDataSource {
    internal val mLoginLiveData: MutableLiveData<com.example.swith.entity.LoginResponse> =
        MutableLiveData<com.example.swith.entity.LoginResponse>()

    fun requestLogin(loginRequest: com.example.swith.entity.LoginRequest): LiveData<com.example.swith.entity.LoginResponse> {
        RetrofitService.retrofitApi.login(loginRequest)
            .enqueue(object : Callback<com.example.swith.entity.LoginResponse> {
                override fun onResponse(
                    call: Call<com.example.swith.entity.LoginResponse>,
                    response: Response<com.example.swith.entity.LoginResponse>
                ) {
                    Log.e("doori", "onResponse = $response")
                    response.body().apply {
                        mLoginLiveData.postValue(this)
                    }
                }

                override fun onFailure(
                    call: Call<com.example.swith.entity.LoginResponse>,
                    t: Throwable
                ) {
                    Log.e("doori", "onFailed = $t")
                }

            })

        return mLoginLiveData
    }
}