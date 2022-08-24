package com.example.swith.repository

import com.example.swith.data.CityResponse
import com.example.swith.data.Regcode
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

object RetrofitService {
    const val baseUrl = "http://192.168.69.85:9000/"
    const val REG_CODE="https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/"

    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client : OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val retrofitApi : RetrofitApi = retrofit.create(RetrofitApi::class.java)
}
interface ApiService{
    @GET("regcodes?")
    fun getCityCode(@Query("regcode_pattern")regcode_pattern: String): Call<CityResponse>
}