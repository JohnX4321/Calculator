package com.tzapps.calculator.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Record::class], version = 1)
abstract class RecordsDatabase: RoomDatabase() {
    abstract fun recordsDao(): RecordDao
}