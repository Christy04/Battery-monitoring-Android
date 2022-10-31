package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.room.Room
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.User
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class BatteryCount : AppCompatActivity() {
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery_count)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Battery"
        ).allowMainThreadQueries().build()

        val timenow = LocalDateTime.now()
        val time5 = LocalDateTime.now().minusDays(5)
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formattednow = timenow.format(formatter)
        val formatted5 = time5.format(formatter)

        var badcount = 0
        var optimalcount = 0
        var spotcount = 0
        var timecount: Long = 0

        val batterylist: List<User> = db.userDao().getcount(formatted5 + "00:00:00", formattednow + "24:59:59")
        val count = batterylist.count()
        if (count != 0) {
            var date = SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(batterylist[0].timestamp)
            val cal = Calendar.getInstance()
            cal.time = date
            var status = batterylist[0].batterystatus
            var level = batterylist[0].batterylevel
            var levelnull: Int = level!!
            var i = 1
            while (i < count) {
                try{

                    var date1 = SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(batterylist[i].timestamp)
                    cal.time = date1

                    if (batterylist[i].batterystatus != status && status == 2 && levelnull != 100) {
                        spotcount++
                    }
                    else if (batterylist[i].batterystatus == status && status == 2 && batterylist[i].batterylevel == 100 && levelnull == 100) {

                        val diff: Long = date1.getTime() - date.getTime()

                        val diffInMin: Long= TimeUnit.MILLISECONDS.toMinutes(diff)
                        timecount += diffInMin
                    }
                    else {

                        if (timecount <= 0) {

                        }
                        else if (timecount < 30) {
                            optimalcount++
                        }
                        else if (timecount >= 30) {
                            badcount++
                        }
                        timecount = 0
                    }
                    status = batterylist[i].batterystatus
                    level = batterylist[i].batterylevel
                    levelnull = level!!
                    date=date1
                    i++
                }
                catch(e: Exception){
                    continue
                }

            }
        }
        val text11=findViewById<TextView>(R.id.badtext)
        val text1=findViewById<TextView>(R.id.badcount)
        text1.setText(badcount.toString())
        val text22=findViewById<TextView>(R.id.optimaltext)
        val text2=findViewById<TextView>(R.id.optimalcount)
        text2.setText(optimalcount.toString())
        val text33=findViewById<TextView>(R.id.spottext)
        val text3=findViewById<TextView>(R.id.spotcount)
        text3.setText(spotcount.toString())



    }
}


