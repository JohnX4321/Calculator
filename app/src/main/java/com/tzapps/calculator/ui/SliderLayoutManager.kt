package com.tzapps.calculator.ui

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.sqrt

class SliderLayoutManager(context: Context) : LinearLayoutManager(context) {

    init {
        orientation= VERTICAL
    }

    interface OnItemSelectedListener {
        fun onItemSelected(layoutPosition: Int)
    }

    var callback: OnItemSelectedListener? = null
    private lateinit var recyclerView: RecyclerView

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        recyclerView = view!!
        LinearSnapHelper().attachToRecyclerView(recyclerView)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        scaleDownView()
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        return if (orientation== VERTICAL) {
            val scrolled = super.scrollVerticallyBy(dy, recycler, state)
            scaleDownView()
            scrolled
        } else
            0
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (state==RecyclerView.SCROLL_STATE_IDLE) {
            val rvCenterY = getRecyclerViewCenterY()
            var minDist = recyclerView.height
            var pos = -1
            for (i in 0 until recyclerView.childCount){
                val c=recyclerView.getChildAt(i)
                var ccY = getDecoratedTop(c)+(getDecoratedBottom(c)-getDecoratedLeft(c))/2
                var newDist = abs(ccY - rvCenterY)
                if (newDist<minDist){
                    minDist = newDist
                    pos = recyclerView.getChildLayoutPosition(c)
                }
            }
            callback?.onItemSelected(pos)
        }
    }

    private fun getRecyclerViewCenterY() = (recyclerView.bottom-recyclerView.top)/2+recyclerView.top

    private fun scaleDownView() {
        val mid = height / 2.0f
        for (i in 0 until childCount) {
            val child = getChildAt(i)!!
            val childMid = (getDecoratedBottom(child) + getDecoratedTop(child)) / 2f
            val distFromCenter = abs(mid-childMid)
            val scale = 1- sqrt((distFromCenter/height).toDouble()).toFloat()*0.66f
            child.scaleX=scale
            child.scaleY=scale
        }
    }

}