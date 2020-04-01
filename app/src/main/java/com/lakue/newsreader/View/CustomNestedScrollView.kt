package com.lakue.newsreader.View

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView
import com.lakue.newsreader.Listener.OnScrollViewStateListener
import com.lakue.newsreader.Type.CustomScrollView

class CustomNestedScrollView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    NestedScrollView(context, attrs, defStyleAttr) {
    private lateinit var onScrollViewStateListener: OnScrollViewStateListener

    override fun onScrollChanged(scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY)

        if (scrollY > this.getChildAt(0).measuredHeight - this.measuredHeight - 3000) {
            onScrollViewStateListener.onScrollViewState(CustomScrollView.BOTTOM)
        }
    }

    fun setOnScrollViewStateListener(onScrollViewStateListener: OnScrollViewStateListener) {
        this.onScrollViewStateListener = onScrollViewStateListener
    }
}
