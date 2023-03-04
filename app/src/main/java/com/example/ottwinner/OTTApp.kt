package com.example.ottwinner

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.multidex.MultiDexApplication
import androidx.work.WorkManager
import com.example.ottwinner.services.TimerService
import com.example.ottwinner.utils.SharedPrefs

class OTTApp : MultiDexApplication(), Application.ActivityLifecycleCallbacks {
    var currentActivity: Activity? = null

    companion object {
        var mInstance: OTTApp? = null

        @Synchronized
        fun getInstance(): OTTApp {
            return mInstance ?: OTTApp()
        }
    }

    override fun onCreate() {
        super.onCreate()

        mInstance = this
        registerActivityLifecycleCallbacks(this)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)

    }

    override fun onLowMemory() {
        super.onLowMemory()
        System.gc()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.e("TAG", "onAppDestroyed: ")
        TimerService.TIMER_CANCEL = true
        val info = WorkManager.getInstance(this).getWorkInfosByTag("task1")
        Log.e("TAG", "onActivityDestroyed: ${info.get()[0].state}")
    }
}