package com.tzapps.calculator.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "records")
data class Record(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var id: Long=System.currentTimeMillis(),
    @ColumnInfo(name = "name")
    var exprName: String,
    @ColumnInfo(name = "expr")
    var exp: String
)