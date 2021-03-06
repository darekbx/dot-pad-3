package com.darekbx.dotpad3.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.darekbx.dotpad3.repository.local.entities.DotDto

@Database(entities = arrayOf(DotDto::class), version = 2, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getDotsDao(): DotsDao

    companion object {
        const val DB_NAME = "dotpad3"
    }
}
