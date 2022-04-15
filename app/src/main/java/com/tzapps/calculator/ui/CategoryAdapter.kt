package com.tzapps.calculator.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(private val categoryList: List<String>,private val switchSubCategory: (Int)->Unit): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1,parent,false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.textView.text = categoryList[position]
        holder.textView.setOnClickListener {
            switchSubCategory(position)
        }
    }

    data class CategoryViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(android.R.id.text1)
    }


}