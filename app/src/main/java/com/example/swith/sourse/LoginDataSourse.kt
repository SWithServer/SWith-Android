package com.example.swith.sourse

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swith.data.LoginRequest
import com.example.swith.data.LoginResponse
import com.example.swith.data.ProfileResponse
import com.example.swith.repository.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginDataSourse {
    internal val mLoginLiveData: MutableLiveData<LoginResponse> = MutableLiveData<LoginResponse>()

    fun requestLogin(loginRequest: LoginRequest) : LiveData<LoginResponse> {
        RetrofitService.retrofitApi.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.e("doori","onResponseLogin = $response")
                Log.e("doori","body = ${response.body()}")
//                response.body().apply {
//                    mLoginLiveData.postValue(this)
//                }
                if (response.body() != null) {
                    Log.e("doori","body not null = ${response.body()}")
                    mLoginLiveData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("doori", "onFailed = $t")
            }

        })

        return mLoginLiveData
    }
}