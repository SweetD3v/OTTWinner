package com.example.ottwinner.ui.activities

import android.Manifest
import android.content.ComponentName
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.ottwinner.R
import com.example.ottwinner.databinding.ActivityMainBinding
import com.example.ottwinner.services.TimerService
import com.example.ottwinner.ui.fragments.HomeFragment
import com.example.ottwinner.ui.fragments.LockedFragment
import com.example.ottwinner.utils.SharedPrefs
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val prefs by lazy { SharedPrefs.newInstance(this) }

    var auth: FirebaseAuth? = null


    var timerTaskRequest: OneTimeWorkRequest? = null

    val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted: Boolean? -> }

    var timerService: TimerService? = null

    var serviceBound = false
    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder: TimerService.TimerServiceBinder = service as TimerService.TimerServiceBinder
            timerService = binder.getService()
            serviceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serviceBound = false
        }

    }

    private fun askForNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            } else {
                // Directly ask for the permission
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        Log.e("TAG", "onResumeTask: ")
        timerTaskRequest?.let {
            TimerService.TIMER_CANCEL = true
        }
        WorkManager.getInstance(this@MainActivity).cancelAllWork()

        resetTimerData()

//        if (serviceBound && timerService != null) {
//            if (timerService!!.timerVal.isNotEmpty()) {
//
//                if (!showedStatus) {
//                    showedStatus = true
//                    toastCustomView("Task Failed", Toast.LENGTH_SHORT, "#D50000")
//                }
//
//                Handler(Looper.getMainLooper())
//                    .postDelayed({
//                        resetTimerData()
//                    }, 2000)
//
//                binding.run {
//                    when (TimerService.txtTimerId) {
//                        1 -> {
//                            txtTimer1.text = timerService!!.timerVal
//                            imgTask1.visible()
//                        }
//
//                        2 -> {
//                            txtTimer2.text = timerService!!.timerVal
//                            imgTask2.visible()
//                        }
//
//                        3 -> {
//                            txtTimer3.text = timerService!!.timerVal
//                            imgTask3.visible()
//                        }
//                    }
//                }
//            } else {
//                binding.run {
//
//                    if (!showedStatus) {
//                        showedStatus = true
//                        toastCustomView("Task Completed!", Toast.LENGTH_SHORT, "#F8C50C")
//                    }
//
//                    when (TimerService.txtTimerId) {
//                        1 -> {
//                            txtTimer1.text = ""
//                            imgTask1.gone()
//                        }
//
//                        2 -> {
//                            txtTimer2.text = ""
//                            imgTask2.gone()
//                        }
//
//                        3 -> {
//                            txtTimer3.text = ""
//                            imgTask3.gone()
//                        }
//                    }
//                }
//            }
//
//            sendBroadcast(Intent(TimerService.CANCEL_TIMER))
//        }
    }

    private fun resetTimerData() {
//        binding.run {
//            txtTimer1.text = ""
//            imgTask1.gone()
//
//            txtTimer2.text = ""
//            imgTask2.gone()
//
//            txtTimer3.text = ""
//            imgTask3.gone()
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val email = intent.getStringExtra("email")
        val displayName = intent.getStringExtra("name")

        Log.e("TAG", "emailName: $email $displayName ")

        askForNotificationPermission()

        val colorScheme = CustomTabColorSchemeParams.Builder().setToolbarColor(
            ContextCompat.getColor(
                this, R.color.white
            )
        ).build()
        val customtabs =
            CustomTabsIntent.Builder().setDefaultColorSchemeParams(colorScheme).build()

        val webList = mutableListOf(
            "com.android.chrome", "com.chrome.beta", "com.chrome.dev"
        )

        val webHelper = CustomTabsClient.getPackageName(
            this, webList
        )

        webHelper?.let {
            customtabs.intent.setPackage(webList[0])
        }

        setupViewPager()

//        binding.run {
//            llTask1.setOnClickListener {
//                customtabs.launchUrl(this@MainActivity, predChampUrl)
//
//                TimerService.txtTimerId = 1
//                showedStatus = false
//                if (!serviceBound) {
//                    val timerIntent = Intent(this@MainActivity, TimerService::class.java)
//                    startService(timerIntent)
//                    bindService(timerIntent, serviceConnection, BIND_AUTO_CREATE)
//                } else {
//                    sendBroadcast(Intent(TimerService.START_TIMER))
//                }
//            }
//
//            llTask2.setOnClickListener {
//                customtabs.launchUrl(this@MainActivity, qurekaUrl)
//                TimerService.txtTimerId = 2
//                showedStatus = false
//                if (!serviceBound) {
//                    val timerIntent = Intent(this@MainActivity, TimerService::class.java)
//                    startService(timerIntent)
//                    bindService(timerIntent, serviceConnection, BIND_AUTO_CREATE)
//                } else {
//                    sendBroadcast(Intent(TimerService.START_TIMER))
//                }
//            }
//
//            llTask3.setOnClickListener {
//                customtabs.launchUrl(this@MainActivity, predChampUrl)
//                TimerService.txtTimerId = 3
//                showedStatus = false
//                if (!serviceBound) {
//                    val timerIntent = Intent(this@MainActivity, TimerService::class.java)
//                    startService(timerIntent)
//                    bindService(timerIntent, serviceConnection, BIND_AUTO_CREATE)
//                } else {
//                    sendBroadcast(Intent(TimerService.START_TIMER))
//                }
//            }
//        }
    }

    var homePageAdapter: HomePageAdapter? = null

    private fun setupViewPager() {
        binding.run {
            homePageAdapter = HomePageAdapter(
                this@MainActivity, arrayListOf(
                    HomeFragment.newInstance(),
                    LockedFragment.newInstance()
                )
            )

            viewPagerHome.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            viewPagerHome.adapter = homePageAdapter
        }
    }

    inner class HomePageAdapter(
        fragmentActivity: FragmentActivity,
        val fragmentsList: ArrayList<Fragment>
    ) :
        FragmentStateAdapter(fragmentActivity) {
        override fun createFragment(position: Int): Fragment {
            return fragmentsList[position]
        }

        override fun getItemCount(): Int {
            return fragmentsList.size
        }
    }

    companion object {
        var showedStatus = false
    }
}