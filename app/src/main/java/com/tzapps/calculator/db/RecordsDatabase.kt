package com.tzapps.calculator.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Record::class], version = 1,exportSchema = false)
abstract class RecordsDatabase: RoomDatabase() {
    abstract fun recordsDao(): RecordDao

    companion object {
        @Volatile private var instance: RecordsDatabase? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDB(context).also { instance=it}
        }
        private fun buildDB(context: Context): RecordsDatabase {
            return Room.databaseBuilder(context,RecordsDatabase::class.java,"CalciRecDB").build()
        }
    }

}