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

    val CATEGORIES = listOf("Length","Area","Temperature","Volume","Mass")//,"Speed","Time")
    val AREA_CATEGORY = listOf("ac","ha","cm^2","ft^2","in^2","m^2")
    val LENGTH_CATEGORY = listOf("m","mm","cm","km","in","ft","yd","mi","NM")
    val TEMP_CATEGORY = listOf("F","C","K")
    val VOLUME_CATEGORY = listOf("gal(US)","gal(UK)","l","ml")
    val WEIGHT_CATEGORY = listOf("kg","g","t","lb","oz")

}