package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.User
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class Dischargetable : AppCompatActivity() {
    private lateinit var db:AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dischargetable)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Battery"
        ).allowMainThreadQueries().build()


        val table=findViewById<TableLayout>(R.id.detailtable)

        var timenow=LocalDateTime.now().minusDays(5)
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        val formatted = timenow.format(formatter)

        var discharge=0
        var timeframe:Long=0
        var cycle=0
        var j=0
        for (k in 1..5) {
            timenow=timenow.plusDays(1)
            val formatted = timenow.format(formatter)

            val batterylist :List<User> = db.userDao().getdischarge(formatted+'%')
            val count=batterylist.count()


            var i=1
            if(count!=0){

                var date = SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(batterylist[0].timestamp)
                val cal = Calendar.getInstance()
                cal.time = date
                var hours = cal.get(Calendar.HOUR_OF_DAY)
                var level =batterylist[0].batterylevel
                var levelnull: Int = level!!
                while(i<count) {
                    val date1 = SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(batterylist[i].timestamp)
                    cal.time = date1
                    var hours1 = cal.get(Calendar.HOUR_OF_DAY)

                    var level1 = batterylist[i].batterylevel
                    var level1null:Int=level1!!

                    if (hours == hours1) {
                        if(levelnull<level1null){
                            levelnull=level1null;
                            date=date1
                        }
                        else{
                            discharge+= (levelnull!!.minus(level1null))
                            val diff: Long = date1.getTime() - date.getTime()
                            val diffInMin: Long= TimeUnit.MILLISECONDS.toMinutes(diff)
                            timeframe+=diffInMin
                        }
                    }
                    if(hours!=hours1 || i==(count-1)){
                        val row=TableRow(this)
                        val layout:TableRow.LayoutParams=TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
                        row.layoutParams=layout
                        var dated=TextView(this)
                        var date_hour=TextView(this)
                        var dis_level=TextView(this)
                        var dis_time=TextView(this)
                        dated.setText(formatted)
                        date_hour.setText(hours.toString())
                        dis_level.setText(discharge.toString())
                        dis_time.setText(timeframe.toString())
                        row.addView(dated)
                        row.addView(date_hour)
                        row.addView(dis_level)
                        row.addView(dis_time)
                        table.addView(row,j)
                        j++
                        cycle+=discharge
                        timeframe=0
                        discharge=0

                    }
                    date=date1
                    hours=hours1
                    levelnull=level1null

                    i++
                }
            }


        }

        cycle/=100
        val text=findViewById<TextView>(R.id.cycletext)
        val text1=findViewById<TextView>(R.id.text1)
        text1.setText(cycle.toString())

    }
}

