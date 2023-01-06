package com.example.swith.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swith.entity.RatingResponse
import com.example.swith.repository.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListDataSource {
    internal val mUserListLiveData: MutableLiveData<RatingResponse> =
        MutableLiveData<RatingResponse>()

    fun requestUserList(
        groupIdx: String,
        userIdx: com.example.swith.entity.ProfileRequest
    ): LiveData<RatingResponse> {
        RetrofitService.retrofitApi.getRating(groupIdx, userIdx)
            .enqueue(object : Callback<RatingResponse> {
                override fun onResponse(
                    call: Call<RatingResponse>,
                    response: Response<RatingResponse>
                ) {
                    Log.e("doori", "onResponse = $response")
                    response.body().apply {
                        mUserListLiveData.postValue(this)
                    }
                }

                override fun onFailure(call: Call<RatingResponse>, t: Throwable) {
                    Log.e("doori", "onFailed = $t")
                }

            })

        return mUserListLiveData
    }
}