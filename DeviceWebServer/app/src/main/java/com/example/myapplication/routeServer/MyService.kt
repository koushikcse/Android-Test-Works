package com.example.myapplication.routeServer

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.myapplication.R

class MyService : Service() {
    private val NOTIFICATION_ID = 0
    private var notificationManager: NotificationManager? = null
    private var webServer: Route? = null

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val port = intent?.getIntExtra("port", 0);
        webServer = port?.let { Route(it) }
        webServer?.start()

        startForeground(1, makeNotification(this))
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager?.cancel(NOTIFICATION_ID)
        stopForeground(true)
        webServer?.stop()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun makeNotification(context: Context): Notification? {
        val intent = Intent(this, ServerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val channelId = getString(R.string.app_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(
            RingtoneManager.TYPE_NOTIFICATION
        )
        val notificationBuilder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        resources,
                        R.mipmap.ic_launcher
                    )
                )
                .setContentTitle(getString(R.string.app_name))
                .setStyle(NotificationCompat.BigTextStyle().bigText("Device web-server running..."))
                .setContentText("Device web-service")
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        val n: Notification
        n = notificationBuilder.build()
        return n
    }
}