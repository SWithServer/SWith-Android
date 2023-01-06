package com.example.swith.repository

import com.example.swith.api.SwithService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Todo : 나중에 지우고 리팩토링 할 코드들
object RetrofitService {
    const val baseUrl = "http://3.39.89.30:9000/"
    const val REG_CODE = "https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/"

    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val retrofitApi: SwithService = retrofit.create(SwithService::class.java)
}

interface ApiService {
    @GET("regcodes?")
    fun getCityCode(@Query("regcode_pattern") regcode_pattern: String): Call<com.example.swith.entity.CityResponse>
}