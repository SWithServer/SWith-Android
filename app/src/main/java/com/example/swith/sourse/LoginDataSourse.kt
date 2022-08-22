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
                Log.e("doori","onResponse = $response")
                response.body()?.apply {
                    mLoginLiveData.postValue(this)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("doori", "onFailed = $t")
            }

        })

        return mLoginLiveData
    }
}