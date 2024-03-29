package com.lakue.newsreader.View

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import com.lakue.newsreader.R

class RoundedImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    ImageView(context, attrs, defStyleAttr, defStyleRes) {
    private val roundRect = RectF()
    private val maskPaint = Paint()
    private val zonePaint = Paint()
    private var rectRadius = 7f

    init {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        val type = context.obtainStyledAttributes(attrs,
            R.styleable.RoundedImageView
        )
        rectRadius = type.getFloat(R.styleable.RoundedImageView_rectRadius, 7f)

        maskPaint.isAntiAlias = true
        maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        zonePaint.isAntiAlias = true
        zonePaint.color = Color.WHITE
        val density = resources.displayMetrics.density
        rectRadius *= density
    }

    fun setRectRadius(radius: Float) {
        rectRadius = radius
        invalidate()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        roundRect.set(0f, 0f, width.toFloat(), height.toFloat())
    }


    override fun draw(canvas: Canvas?) {
        canvas?.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG)
        canvas?.drawRoundRect(roundRect, rectRadius, rectRadius, zonePaint)
        canvas?.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG)
        super.draw(canvas)
        canvas?.restore()
    }
}
