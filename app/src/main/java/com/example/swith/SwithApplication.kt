package com.example.swith

import android.app.Application
import android.util.Log
import com.example.swith.utils.FirebaseMessageService
import com.example.swith.utils.NoticeManager
import com.google.firebase.messaging.FirebaseMessaging
import com.example.swith.utils.SharedPrefManager
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility


class SwithApplication:Application() {
    companion object{
        lateinit var spfManager : SharedPrefManager
    }
    override fun onCreate() {
        super.onCreate()
        val keyHash = Utility.getKeyHash(this)
        Log.e("doori", "HashKey: ${keyHash}")
        spfManager = SharedPrefManager(applicationContext)
        // Kakao SDK 초기화
        KakaoSdk.init(this,"1cdb9c58056551e093411bcab081a6b4" )
        FirebaseMessageService().getFirebaseToken()

        //NoticeManager(this@SwithApplication).runRatingNotice("asdasd","aaaaa","2")

    }
}