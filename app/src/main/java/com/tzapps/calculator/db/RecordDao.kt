package com.tzapps.calculator.db

import androidx.room.*

@Dao
interface RecordDao {

    @Query("SELECT * FROM records")
    fun getAll(): List<Record>

    @Insert
    fun insert(record: Record)

    @Delete
    fun delete(record: Record)

    @Query("DELETE FROM records")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM records")
    fun countAll(): Int

}