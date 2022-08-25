package com.example.swith

import android.app.Application
import android.util.Log
<<<<<<< Updated upstream
import com.example.swith.utils.FirebaseMessageService
import com.example.swith.utils.NoticeManager
import com.google.firebase.messaging.FirebaseMessaging
=======
import com.example.swith.utils.SharedPrefManager
>>>>>>> Stashed changes
//import com.example.swith.BuildConfig.KAKAO_API_KEY
//import com.example.swith.BuildConfig.KAKAO_API_KEY
// import com.example.swith.BuildConfig.KAKAO_API_KEY
import com.kakao.sdk.common.KakaoSdk


class SwithApplication:Application() {
    companion object{
        lateinit var spfManager : SharedPrefManager
    }
    override fun onCreate() {
        super.onCreate()
        spfManager = SharedPrefManager(applicationContext)
        // Kakao SDK 초기화
        KakaoSdk.init(this,"1cdb9c58056551e093411bcab081a6b4" )
        FirebaseMessageService().getFirebaseToken()

        //NoticeManager(this@SwithApplication).runRatingNotice("asdasd","aaaaa","2")

    }
}