package com.example.ottwinner.timer_helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.ottwinner.R
import com.example.ottwinner.services.TimerService
import com.example.ottwinner.ui.activities.MainActivity
import com.example.ottwinner.utils.SharedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch


class TimerWorker(
    ctx: Context,
    workerParams: WorkerParameters
) :
    CoroutineWorker(ctx.applicationContext, workerParams) {
    var jobClassify: Job? = null

    val channelId = "classify_channel"
    val channelName = "classify_name"

    var count = 60

    var seconds = 0
    var minute: Int = 0

    var handler = Handler(Looper.getMainLooper())
    var runnable = object : Runnable {
        override fun run() {
            showNotification(ctx.applicationContext, count)
            handler.postDelayed(this, 1000)
            count--

            if (count <= 0 || TimerService.TIMER_CANCEL) {
                seconds = 0
                count = 60
                minute = 0
                handler.removeCallbacks(this)
                jobClassify?.cancel()
                manager?.cancel(1)
                WorkManager.getInstance(ctx.applicationContext).cancelAllWork()
            }
        }
    }

    var manager: NotificationManager? = null
    private val remoteViews by lazy {
        RemoteViews(
            ctx.packageName,
            R.layout.timer_notification_layout
        )
    }

    override suspend fun doWork(): Result {
        manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            manager?.createNotificationChannel(channel)
        }

        handleTimer()
        return Result.success()
    }

    private fun handleTimer() {
        if (jobClassify != null) return

        jobClassify = GlobalScope.launch(Dispatchers.Main) {
            handler.post(runnable)
        }
    }

    private fun showNotification(ctx: Context, progress: Int) {
        Log.e("TAG", "progress: ${progress}")

        minute = progress / 60
        seconds = if (minute == 1) 0 else progress

        val timerVal = "$minute : ${if (seconds > 9) "$seconds" else "0$seconds"}"

        remoteViews.setTextViewText(R.id.txtTimer, timerVal)

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channelId)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContent(remoteViews)
                .setContentTitle("Timer")
                .setAutoCancel(false)
                .setOngoing(true)
                .setSound(null)
                .setVibrate(null)
                .setOnlyAlertOnce(false)
                .setSmallIcon(
                    R.mipmap.ic_launcher
                )
        manager?.notify(1, builder.build())
    }
}