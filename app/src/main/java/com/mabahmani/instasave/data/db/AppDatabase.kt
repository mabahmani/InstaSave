package com.mabahmani.instasave.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mabahmani.instasave.data.db.dao.DownloadDao
import com.mabahmani.instasave.data.db.entity.DownloadEntity

@Database(entities = [DownloadEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun downloadDao(): DownloadDao
}