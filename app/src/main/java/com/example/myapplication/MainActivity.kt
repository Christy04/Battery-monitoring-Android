package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.myapplication.data.AppDatabase


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        startService(Intent(this,InsertionService::class.java))

        val btn = findViewById<Button>(R.id.button_id)
        btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(applicationContext,Dischargetable::class.java))
            }
        })
        val btn1 = findViewById<Button>(R.id.count_id)
        btn1.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(applicationContext,BatteryCount::class.java))
            }
        })
    }

}
