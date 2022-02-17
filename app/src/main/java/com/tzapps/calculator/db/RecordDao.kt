package com.tzapps.calculator.db

import androidx.room.*

@Dao
interface RecordDao {

    @Query("SELECT * FROM records ORDER BY id ASC")
    suspend fun getAll(): List<Record>

    @Insert
    suspend fun insert(record: Record)

    @Delete
    suspend fun delete(record: Record)

    @Query("DELETE FROM records")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM records")
    suspend fun countAll(): Int

}