package com.nitish.privacyindicator.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nitish.privacyindicator.models.AccessLog
import com.nitish.privacyindicator.models.SuspiciousActivity

@Database(
        entities = [AccessLog::class, SuspiciousActivity::class],
        version = 2
)
@TypeConverters(Converters::class)
abstract class AccessLogsDatabase: RoomDatabase() {

    abstract fun getAccessLogsDao():AccessLogsDao
    abstract fun getSuspiciousActivitiesDao(): SuspiciousActivitiesDao

    companion object {
        @Volatile
        private var instance: AccessLogsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
                Room.databaseBuilder(
                        context.applicationContext,
                        AccessLogsDatabase::class.java,
                        "access_logs_db.db"
                ).fallbackToDestructiveMigration()
                    .build()
    }
}