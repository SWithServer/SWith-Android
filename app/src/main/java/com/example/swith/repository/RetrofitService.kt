package com.example.swith.repository

import com.example.swith.data.CityResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object RetrofitService {
    const val baseUrl = " "
    const val REG_CODE="https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/"

    val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
    }
}
interface ApiService{
    @GET("regcodes?")
    fun getCityCode(@Query("regcode_pattern")regcode_pattern: String): Call<CityResponse>
}