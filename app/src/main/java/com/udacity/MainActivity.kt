package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*



class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    var selected=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        createChannel("Download","Download")
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        val radioGroup=findViewById<RadioGroup>(R.id.group)



        custom_button.setOnClickListener {
            val id  = radioGroup.checkedRadioButtonId
            when(id){
                -1->{ // return -1 when none selected
                    Toast.makeText(applicationContext,"Choose one option",Toast.LENGTH_LONG).show()

                }else->{
                val selectedItem = findViewById<RadioButton>(id)
                Log.d("1step","h")
                download(selectedItem.text.toString())
            }
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val query = DownloadManager.Query()
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            notificationManager = ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager

            query.setFilterById(id!!)

            val cursor = downloadManager.query(query)

            if (cursor.moveToFirst()){
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                when (status) {
                    DownloadManager.STATUS_SUCCESSFUL ->{
                        notificationManager.sendNotification(
                            applicationContext.getString(R.string.notification_description),
                            applicationContext,selected,"Success")
                    }
                    DownloadManager.STATUS_FAILED -> {
                        notificationManager.sendNotification(
                            applicationContext.getString(R.string.notification_description),
                            applicationContext,selected,"Failed")
                    }

                }
            }



        }
    }
    private fun download(name:String) {
    var url=""
    when(name){
        "Glide-Image Loading Library"-> {
            url=URL1
            selected=name
        }
        "Retrofit-HTTP Client for android and java"-> {
            url=URL3
            selected=name
        }
        "Load app-Current Repo by udacity"-> {
            url=URL2
            selected=name
        }
    }
        val notificationManager =
            ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager
        notificationManager.cancelNotifications()
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(name)
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request) // enqueue puts the download request in the queue.
    }

    companion object {
        private const val URL1 =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"

        private const val URL2="https://github.com/bumptech/glide"
        private const val URL3="https://github.com/square/retrofit"
        private const val CHANNEL_ID = "channelId"
    }


    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Time for breakfast"


            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


}


