package com.example.swith.data.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swith.domain.entity.RatingResponse
import com.example.swith.data.api.RetrofitService
import com.example.swith.domain.entity.ProfileRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RatingDataSource {
    internal val mRatingLiveData: MutableLiveData<RatingResponse> =
        MutableLiveData<RatingResponse>()

    fun requestRating(
        groupIdx: String,
        userIdx: ProfileRequest
    ): LiveData<RatingResponse> {
        RetrofitService.retrofitApi.getRating(groupIdx, userIdx)
            .enqueue(object : Callback<RatingResponse> {
                override fun onResponse(
                    call: Call<RatingResponse>,
                    response: Response<RatingResponse>
                ) {
                    Log.e("doori", "onResponse = $response")
                    response.body().apply {
                        mRatingLiveData.postValue(this)
                    }
                }

                override fun onFailure(call: Call<RatingResponse>, t: Throwable) {
                    Log.e("doori", "onFailed = $t")
                }

            })

        return mRatingLiveData
    }
}