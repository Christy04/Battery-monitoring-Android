package com.example.myapplication


import android.R
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class InsertionService:Service()
{

    private lateinit var db:AppDatabase
    override fun onCreate() {

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Battery"
        ).allowMainThreadQueries().build()
        super.onCreate()
    }
    private val NOTIF_ID = 1
    private val NOTIF_CHANNEL_ID = "Channel_Id"

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        this.registerReceiver(myBroadcastReceiver,ifilter)
        startForeground()
        return super.onStartCommand(intent, flags, startId)
    }




    private val myBroadcastReceiver=object:BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {


            if(intent?.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
                val bm = applicationContext.getSystemService(BATTERY_SERVICE) as BatteryManager
                val batLevel: Int = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

                val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                val batteryStatus = context!!.registerReceiver(null, ifilter)
                val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
                val isCharging: Boolean =
                    status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
                val batterystatus: Int
                if (isCharging)
                    batterystatus = 2
                else
                    batterystatus = 1
                val current = LocalDateTime.now()

                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                val formatted = current.format(formatter)
                val user = User(0, batterystatus, batLevel, formatted.toString())
                db.userDao().insertAll(user)
                //Log.i("msg","Hello")
            }
        }
    }
    private fun startForeground() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent, 0
        )
        val chan = NotificationChannel(

            NOTIF_CHANNEL_ID,
            "Foreground Service",
            NotificationManager.IMPORTANCE_LOW
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_SECRET
        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        startForeground(
            NOTIF_ID, NotificationCompat.Builder(
                this,
                NOTIF_CHANNEL_ID
            )
                .setOngoing(true)
                .setContentTitle("Battery-Monitor")
                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)
                .build()
        )
    }
}