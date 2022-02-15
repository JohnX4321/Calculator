package com.tzapps.calculator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tzapps.calculator.db.Record

class HistoryAdapter(var recordsList: List<Record>): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun getItemCount(): Int {
        return recordsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_expression,null)
        //xml encoding didn't worked. this did.
        v.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        return HistoryViewHolder(v)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.exprTV.text=recordsList[position].exprName
        holder.resTV.text=recordsList[position].exp
    }



    inner class HistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val exprTV = itemView.findViewById<TextView>(R.id.txtExpr)!!
        val resTV = itemView.findViewById<TextView>(R.id.txtResult)!!


    }

}