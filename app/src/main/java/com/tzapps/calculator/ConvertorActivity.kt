package com.tzapps.calculator

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.tzapps.calculator.utils.Utils

class ConvertorActivity: AppCompatActivity() {

    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var subcategoryRecyclerView: RecyclerView
    private lateinit var convertButton: AppCompatButton
    private lateinit var switchButton: AppCompatImageButton
    private lateinit var fromLabel: TextView
    private lateinit var toLabel: TextView
    private lateinit var fromEditText: EditText
    private lateinit var toEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convertor)
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView)!!
        subcategoryRecyclerView = findViewById(R.id.subcategoryRecyclerView)!!
        convertButton = findViewById(R.id.convertButton)!!
        switchButton = findViewById(R.id.switchConversion)!!
        fromLabel = findViewById(R.id.fromId)!!
        toLabel = findViewById(R.id.toId)!!
        fromEditText = findViewById(R.id.fromInput)!!
        toEditText = findViewById(R.id.toInput)!!
        val padding = Utils.getScreenHeight(this)/2-Utils.dpToPx(this,40)
        categoryRecyclerView.setPadding(0,padding,0,padding)
        subcategoryRecyclerView.setPadding(0,padding,0,padding)

    }

}