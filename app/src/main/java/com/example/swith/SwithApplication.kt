package com.example.swith

import android.app.Application
import android.util.Log
import com.example.data.utils.FirebaseMessageService
import com.example.data.utils.SharedPrefManager
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
        KakaoSdk.init(this, _root_ide_package_.com.example.swith.BuildConfig.KAKAO_NATIVE_APP_KEY)
        FirebaseMessageService().getFirebaseToken()
    }
}