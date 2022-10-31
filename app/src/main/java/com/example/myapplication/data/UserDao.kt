package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM Batteryinfo")
    fun getAll(): List<User>

    @Query("SELECT * FROM Batteryinfo where timestamp like :giventime and batterystatus=1")
    fun getdischarge(giventime:String?): List<User>

    @Query("SELECT distinct * FROM Batteryinfo where timestamp between :giventime and :timenow")
    fun getcount(giventime:String,timenow:String): List<User>

    @Insert
    fun insertAll(vararg users: User)

    @Query("DELETE FROM batteryinfo WHERE id = :userId")
    fun deleteByUserId(userId: Int)

}