package com.lakue.newsreader.Base

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue

object BaseUtil {

    /**
     * px to dp
     * @param px
     * @param context
     * @return
     */
    fun convertPixelsToDp(px: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            px,
            context.resources.displayMetrics
        ).toInt()
    }

    /**
     * dp to px
     * @param context
     * @param dp
     * @return
     */
    fun dpToPx(context: Context, dp: Float): Float {
        val dm = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm).toInt().toFloat()
    }

    /**
     * get device width
     * @param context
     * @return
     */
    fun getDeviceWidth(context: Context): Int {
        val dm = context.resources.displayMetrics
        return dm.widthPixels
    }

    /**
     * get device height
     * @param context
     * @return
     */
    fun getDeviceHeight(context: Context): Int {
        val dm = context.resources.displayMetrics
        return dm.heightPixels
    }

}
