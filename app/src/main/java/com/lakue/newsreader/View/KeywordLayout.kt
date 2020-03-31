package com.lakue.newsreader.View

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import com.lakue.newsreader.Base.BaseUtil
import com.lakue.newsreader.R

class KeywordLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        initView()
    }
    val count: Int
        get() = childCount

    private fun initView() {
        orientation = VERTICAL
    }

    fun addKeyWord(vararg keyword: String) {
//        if (count > 2) {
//            return
//        }
        val ll = LinearLayout(context)
        ll.orientation = HORIZONTAL

        //키워드 생성
        for (s in keyword) {
            val textView = TextView(context)
            val layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
            )

            layoutParams.setMargins(
                0, 0,
                BaseUtil.convertPixelsToDp(8f, context),
                BaseUtil.convertPixelsToDp(8f, context)
            )
            textView.text = s
            textView.maxWidth = BaseUtil.getDeviceWidth(context) / 3   //키워드 하나의 크기 = 디바이스 가로크기/3
            textView.layoutParams = layoutParams
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setTextColor(context.getColor(R.color.colorLightBlack))
                textView.setPadding(
                    BaseUtil.convertPixelsToDp(12f, context),
                    BaseUtil.convertPixelsToDp(4f, context),
                    BaseUtil.convertPixelsToDp(12f, context),
                    BaseUtil.convertPixelsToDp(4f, context)
                )
                textView.textSize = 12f
                textView.gravity = TextView.TEXT_ALIGNMENT_CENTER
                textView.background =
                    context.getDrawable(R.drawable.background_rounding_20_white_stroke_black)
            }

            ll.addView(textView)
        }

        addView(ll)

    }

    fun reset() {
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            removeView(view)
        }
    }

}
