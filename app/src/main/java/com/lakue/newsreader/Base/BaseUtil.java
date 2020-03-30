package com.lakue.newsreader.Base;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class BaseUtil {

    /**
     *
     * @param px
     * @param context
     * @return
     */
    public static int convertPixelsToDp(float px, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources().getDisplayMetrics());
    }

    /**
     *
     * @param context
     * @param dp
     * @return
     */
    public static float dpToPx(Context context, float dp) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
    }

    public static int getDeviceWidth(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getDeviceHeight(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

}
