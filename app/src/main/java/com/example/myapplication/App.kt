package com.example.myapplication

import android.app.Application
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

public class App :Application() {
    override fun onCreate() {
        super.onCreate()
        startService(Intent(this,InsertionService::class.java))
    }
}