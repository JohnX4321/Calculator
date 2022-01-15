package com.tzapps.calculator.db

import android.content.Context
import androidx.room.Room

class RecordDB(val context: Context) {

    var appDB = Room.databaseBuilder(context,RecordsDatabase::class.java,"CalciRecDB")


}