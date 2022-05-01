package com.tzapps.calculator

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import com.tzapps.calculator.utils.Utils

class ConverterAdapter(private val listener: RecyclerItemClickListener): RecyclerView.Adapter<ConverterAdapter.TextViewHolder>() {

    companion object {
        var lastSelectedIndex = -1
    }

    interface RecyclerItemClickListener {
        fun onRecyclerItemClick(pos: Int)
    }

    val dataList = Utils.CATEGORIES

    inner class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(android.R.id.text1)
        init {
            textView.setOnClickListener {
                val a = adapterPosition
                listener.onRecyclerItemClick(a)
                textView.text = textView.text.toString()+"  \u2713"
                if (lastSelectedIndex!=-1)
                    notifyItemChanged(lastSelectedIndex)
                lastSelectedIndex=a
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        return TextViewHolder(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1,parent,false))
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        holder.textView.text = dataList[position]
        if (position==0&&lastSelectedIndex==-1) {
            holder.textView.performClick()
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}