package com.example.ottwinner.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.ottwinner.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class TimerService : Service() {
    var binder: IBinder = TimerServiceBinder()

    var jobClassify: Job? = null

    val channelId = "classify_channel"
    val channelName = "classify_name"

    var count = 10

    var seconds = 0
    var minute: Int = 0

    var timerVal = ""

    var handler = Handler(Looper.getMainLooper())
    var runnable = object : Runnable {
        override fun run() {
            showNotification(count)
            handler.postDelayed(this, 1000)
            count--

            if (count <= 0 || TIMER_CANCEL) {
                stopTimer()
            }
        }
    }

    var manager: NotificationManager? = null
    private val remoteViews by lazy {
        RemoteViews(
            this@TimerService.packageName,
            R.layout.timer_notification_layout
        )
    }

    val cancelTimerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            TIMER_CANCEL = true
            stopTimer()
        }
    }

    val startTimerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            TIMER_CANCEL = false
            startTimer()
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    inner class TimerServiceBinder : Binder() {
        fun getService(): TimerService {
            return this@TimerService
        }
    }

    override fun onCreate() {
        super.onCreate()

        registerReceiver(startTimerReceiver, IntentFilter(START_TIMER))
        registerReceiver(cancelTimerReceiver, IntentFilter(CANCEL_TIMER))

        manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            manager?.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e("TAG", "onStartCommand: ${txtTimerId}")

        startTimer()
        return START_NOT_STICKY
    }

    private fun startTimer() {
        TIMER_CANCEL = false
        jobClassify = GlobalScope.launch(Dispatchers.Main) {
            handler.post(runnable)
        }
    }

    private fun showNotification(progress: Int) {
        Log.e("TAG", "progress: ${progress}")

        minute = progress / 60
        seconds = if (minute == 1) 0 else progress

        val timerVal = "$minute : ${if (seconds > 9) "$seconds" else "0$seconds"}"
        this.timerVal = timerVal

        remoteViews.setTextViewText(R.id.txtTimer, timerVal)

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContent(remoteViews)
                .setContentText("Timer")
                .setContentTitle("Timer")
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setSmallIcon(
                    R.mipmap.ic_launcher
                )
        manager?.notify(1, builder.build())
    }

    fun stopTimer() {
        timerVal = ""
        seconds = 0
        count = 10
        minute = 0
        cancelNotification()
        handler.removeCallbacks(runnable)
        jobClassify?.cancel()
        manager?.cancel(1)
    }

    fun cancelNotification() {
        manager?.cancel(1)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
        unregisterReceiver(startTimerReceiver)
        unregisterReceiver(cancelTimerReceiver)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.e("TAG", "onTaskRemoved: ")
        stopTimer()
    }

    companion object {
        const val CANCEL_TIMER = "com.example.ottwinner.services.CANCEL_TIMER"
        const val START_TIMER = "com.example.ottwinner.services.START_TIMER"
        var txtTimerId = 0
        var TIMER_CANCEL = false
    }
}