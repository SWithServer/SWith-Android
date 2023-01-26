package com.example.swith

import android.app.Application
import android.util.Log
import com.example.swith.utils.FirebaseMessageService
import com.example.swith.utils.SharedPrefManager
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SwithApplication : Application() {
    companion object {
        lateinit var spfManager: SharedPrefManager
    }

    override fun onCreate() {
        super.onCreate()
        spfManager = SharedPrefManager(applicationContext)

        val keyHash = Utility.getKeyHash(this)
        Log.e("keyHash", "HashKey: ${keyHash}")
        // Kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        FirebaseMessageService().getFirebaseToken()
    }
}