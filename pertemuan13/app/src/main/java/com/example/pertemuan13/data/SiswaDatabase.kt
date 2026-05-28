package com.example.pertemuan13.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Siswa::class],
    version = 1,
    exportSchema = false
)
abstract class SiswaDatabase : RoomDatabase() {

    abstract fun siswaDao(): SiswaDao

    companion object {
        @Volatile
        private var INSTANCE: SiswaDatabase? = null

        fun getDatabase(context: Context): SiswaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SiswaDatabase::class.java,
                    "siswa_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
