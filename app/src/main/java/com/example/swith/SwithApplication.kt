package com.example.swith

import android.app.Application
import android.util.Log
//import com.example.swith.BuildConfig.KAKAO_API_KEY
//import com.example.swith.BuildConfig.KAKAO_API_KEY
// import com.example.swith.BuildConfig.KAKAO_API_KEY
import com.kakao.sdk.common.KakaoSdk


class SwithApplication:Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao SDK 초기화
//        KakaoSdk.init(this,"1cdb9c58056551e093411bcab081a6b4" )
        //Log.e("doori", "kakao1cdb9c58056551e093411bcab081a6b4")
    }
}