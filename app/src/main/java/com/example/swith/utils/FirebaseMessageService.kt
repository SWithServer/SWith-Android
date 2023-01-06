package com.example.swith.utils

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessageService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("doori", "fmc token = $token")
        SharedPrefManager(this@FirebaseMessageService).setFcmToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.notification?.title ?: "오류"
        val content = message.notification?.body!!.split("//").get(0)
        val subject = message.notification?.body!!.split("//").get(1)
        Log.e("doori", "title = $title, content = $content, subject = $subject")
        if (subject == "종료") {
            val groupId = message.notification?.body!!.split("//").get(2)
            Log.e(
                "doori",
                "title = $title, content = $content, subject = $subject , groupId = $groupId"
            )
            //oticeManager(this@FirebaseMessageService).runRatingNotice(title,content,groupId)
        } else if (subject == "공지") {
            val groupId = message.notification?.body ?: "오류".split("//").get(2)
            Log.e(
                "doori",
                "title = $title, content = $content, subject = $subject , groupId = $groupId"
            )
            NoticeManager(this@FirebaseMessageService).runAnnounceNotice(title, content)
        } else if (subject == "알림") {
            Log.e("doori", "title = $title, content = $content, subject = $subject ")
            //NoticeManager(this@FirebaseMessageService).runAlertNotice(title,content)
        }
        //Log.e("doori","fcm message notification body = ${message.notification?.body}")
        //Log.e("doori","fcm message title= ${message.notification?.title}")
    }

    /** Token 가져오기 */
    fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.d("doori", "token=${it}")
        }
    }
}