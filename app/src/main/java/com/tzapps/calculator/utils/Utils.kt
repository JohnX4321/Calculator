package com.tzapps.calculator.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager

object Utils {

    fun getScreenWidth(context: Context): Int {
        val wm = if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M) context.getSystemService(Context.WINDOW_SERVICE) as WindowManager else context.getSystemService(WindowManager::class.java)
        var dm = DisplayMetrics()
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.R)
            dm=Resources.getSystem().displayMetrics
        else
            wm.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        val wm = if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M) context.getSystemService(Context.WINDOW_SERVICE) as WindowManager else context.getSystemService(WindowManager::class.java)
        val dm = DisplayMetrics()
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.R)
            context.display?.getMetrics(dm)
        else
            wm.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }

    fun dpToPx(context: Context, value: Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value.toFloat(),context.resources.displayMetrics).toInt()

    val CATEGORIES = listOf<String>("Area","Length","Temperature","Volume","Mass","Speed","Time")
    val SUBCATEGORIES = listOf(listOf("ac","a","ha","cm^2","ft^2","in^2","m^2"),
        listOf("m","mm","cm","km","in","ft","yd","mi","NM"), listOf("F","C","K"), listOf("gal(US)","l","ml","cc","m^3","in^3","ft^3"),
    listOf("kg","t","lb","oz","kg","g"))

}