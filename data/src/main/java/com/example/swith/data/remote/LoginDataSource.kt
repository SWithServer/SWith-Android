package com.example.swith.data.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swith.data.api.RetrofitService
import com.example.swith.domain.entity.LoginRequest
import com.example.swith.domain.entity.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginDataSource {
    internal val mLoginLiveData: MutableLiveData<LoginResponse> =
        MutableLiveData<LoginResponse>()

    fun requestLogin(loginRequest: LoginRequest): LiveData<LoginResponse> {
        RetrofitService.retrofitApi.login(loginRequest)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    Log.e("doori", "onResponse = $response")
                    response.body().apply {
                        mLoginLiveData.postValue(this)
                    }
                }

                override fun onFailure(
                    call: Call<LoginResponse>,
                    t: Throwable
                ) {
                    Log.e("doori", "onFailed = $t")
                }

            })

        return mLoginLiveData
    }
}