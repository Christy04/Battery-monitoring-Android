package com.example.myapplication.data
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "Batteryinfo")
data class User (
    @PrimaryKey(autoGenerate = true) val id:Int,
    @ColumnInfo(name = "batterystatus") val batterystatus:Int?,
    @ColumnInfo(name = "batterylevel") val batterylevel:Int?,
    @ColumnInfo(name = "timestamp") val timestamp: String
)