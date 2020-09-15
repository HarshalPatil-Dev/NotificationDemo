package com.sample.notificationdemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val channelId = "com.sample.notificationdemo.channel1"
    private var notificationManager: NotificationManager? = null
    private val KEY_REPLY = "key_reply"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(channelId, "DemoChannel", "Notification Demo")
        button.setOnClickListener {
            displayNotification()
        }
    }

    private fun displayNotification() {
        val notificationId = 1

        var tapResultIntent = Intent(this, SecondActivity::class.java)//.apply {
        // flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        // }
        var pendingIntent: PendingIntent = PendingIntent.getActivity(this,
                0,
                tapResultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        //Details Action Button
        var intent2 = Intent(this, DetailsActivity::class.java)
        var pendingIntent2: PendingIntent = PendingIntent.getActivity(this,
                0,
                intent2,
                PendingIntent.FLAG_UPDATE_CURRENT)

        val action2: NotificationCompat.Action = NotificationCompat.Action.Builder(0, "Details", pendingIntent2).build()

        //Settings Button
        var intent3 = Intent(this, SettingsActivity::class.java)
        var pendingIntent3: PendingIntent = PendingIntent.getActivity(this,
                0,
                intent3,
                PendingIntent.FLAG_UPDATE_CURRENT)

        val action3: NotificationCompat.Action = NotificationCompat.Action.Builder(0, "Settings", pendingIntent3).build()

        //reply action
        val remoteInput: RemoteInput = RemoteInput.Builder(KEY_REPLY).run {
            setLabel("Insert Your Name")
            build()
        }
        val replyAction = NotificationCompat.Action.Builder(
                0,
                "Reply",
                pendingIntent
        ).addRemoteInput(remoteInput)
                .build()

        val notification = NotificationCompat.Builder(this@MainActivity, channelId)
                .setContentTitle("Demo Title")
                .setContentText("This is Demo Notification")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                 //.setContentIntent(pendingIntent)
                .addAction(action2)
                .addAction(action3)
                .addAction(replyAction)
                .build()
        notificationManager?.notify(notificationId, notification)

    }


    private fun createNotificationChannel(id: String, name: String, channelDiscroption: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance).apply {
                description = channelDiscroption
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }
}