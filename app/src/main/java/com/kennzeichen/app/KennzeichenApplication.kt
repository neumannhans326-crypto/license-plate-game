package com.kennzeichen.app

import android.app.Application
import androidx.room.Room
import com.kennzeichen.app.data.database.AppDatabase

class KennzeichenApplication : Application() {

    private var database: AppDatabase? = null
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "kennzeichen-db"
        ).fallbackToDestructiveMigration().build()
    }

    fun getDatabase(): AppDatabase = database!!
}