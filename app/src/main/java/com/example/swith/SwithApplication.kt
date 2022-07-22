package com.example.swith

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class SwithApplication:Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao SDK 초기화
        KakaoSdk.init(this, "1cdb9c58056551e093411bcab081a6b4")
    }
}