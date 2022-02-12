package com.poid.marveldemoapp.data.local

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.poid.marveldemoapp.data.local.entity.ComicsEntity
import com.poid.marveldemoapp.data.local.entity.ComicsEntityFTS

@Database(
    entities = [
        ComicsEntity::class,
        ComicsEntityFTS::class,
    ], version = 1
)
abstract class ComicsDataBase : RoomDatabase() {
    abstract fun comicsDao(): ComicsDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ComicsDataBase? = null
        private const val dataBaseName = "database-name"

        fun getDatabase(applicationContext: Application): ComicsDataBase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    applicationContext,
                    ComicsDataBase::class.java,
                    dataBaseName
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}