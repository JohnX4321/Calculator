package com.tzapps.calculator.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "records")
data class Record(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "name")
    var exprName: String,
    @ColumnInfo(name = "expr")
    var exp: String
)