package com.lakue.newsreader.Activity

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import com.lakue.newsreader.Base.BaseActivity
import com.lakue.newsreader.Base.LoadingDialog
import com.lakue.newsreader.DataNewsFeed
import com.lakue.newsreader.R
import kotlinx.android.synthetic.main.activity_news_detail.*

class ActivityNewsDetail : BaseActivity() {

    lateinit var data: DataNewsFeed
    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        if (intent.hasExtra("EXTRA_NEWS_FEED")) {
            data = intent.getSerializableExtra("EXTRA_NEWS_FEED") as DataNewsFeed
        }

        tv_title.text = data.title
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
        }
        loadingDialog!!.progressON(this, "Loading...", true)
        when {
            data.keywords.size == 0 -> kl_keyword.visibility = View.GONE
            data.keywords.size == 1 -> {
                kl_keyword.visibility = View.VISIBLE
                kl_keyword.addKeyWord(data.keywords[0])
            }
            data.keywords.size == 2 -> {
                kl_keyword.visibility = View.VISIBLE
                kl_keyword.addKeyWord(data.keywords[0], data.keywords[1])
            }
            data.keywords.size > 3 -> {
                kl_keyword.visibility = View.VISIBLE
                kl_keyword.addKeyWord(data.keywords[0], data.keywords[1], data.keywords[2])
            }
        }

        wv_news_feed.settings.javaScriptEnabled = true//자바스크립트 허용

        wv_news_feed.loadUrl(data.link)//웹뷰 실행
        wv_news_feed.settings.useWideViewPort = true
        wv_news_feed.settings.loadWithOverviewMode = true // wide viewport를 사용하도록 설정
        wv_news_feed.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        wv_news_feed.settings.setSupportZoom(false) // 화면 줌 허용 여부
        wv_news_feed.settings.builtInZoomControls = false // 화면 확대 축소 허용 여부
        wv_news_feed.webChromeClient = WebChromeClient()//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
        wv_news_feed.webViewClient = WebViewClientClass()
    }

    private inner class WebViewClientClass : WebViewClient() {
        //페이지 이동
        // 링크 클릭에 대한 반응
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            Log.d("check URL", url)
            view.loadUrl(url)
            return true
        }

        override fun onReceivedError(view: WebView?,request: WebResourceRequest?,error: WebResourceError?) {
           // showToast("오류 : $request")
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

        }

        override fun onPageFinished(view: WebView?, url: String?) {
            loadingDialog!!.progressOFF(500)
        }
    }
}
