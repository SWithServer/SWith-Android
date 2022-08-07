package com.example.swith.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.example.swith.R
import com.example.swith.ui.study.announce.AnnounceActivity


class NoticeManager(private val context: Context) {
    companion object {
        private const val CHANNEL_ID = "notice"
        private const val CHANNEL_NAME: String = "noticeChannel"
        private const val CHANNEL_NUMBER = 11
    }

    var manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    private lateinit var builder: NotificationCompat.Builder

    init {
        createChannel()

    }

    private fun createChannel() {
        val channelId = CHANNEL_ID
        val channelName = CHANNEL_NAME
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )

        channel.setShowBadge(true)
        //TODO 알람소리, 진동은 어떻게 할 것 인가?
        channel.enableLights(true)
        channel.lightColor = Color.RED

        manager.createNotificationChannel(channel)

        builder = NotificationCompat.Builder(context, channelId)

    }

    fun runNotice(title: String,content: String){
        val actionIntent = Intent(context,AnnounceActivity::class.java)
        val actionPendingIntent = PendingIntent.getActivity(context, CHANNEL_NUMBER,actionIntent,PendingIntent.FLAG_IMMUTABLE)

        builder.run {
            setSmallIcon(R.drawable.ic_stat_notification)
            setWhen(System.currentTimeMillis())
            setContentTitle(title)
            setContentText(content)
            setContentIntent(actionPendingIntent)

            manager.notify(CHANNEL_NUMBER,build())
        }
    }
}