package com.lakue.newsreader.Activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import com.lakue.newsreader.Base.BaseActivity
import com.lakue.newsreader.Base.LoadingDialog
import com.lakue.newsreader.Data.DataNewsFeed
import com.lakue.newsreader.R
import kotlinx.android.synthetic.main.activity_news_detail.*

class ActivityNewsDetail : BaseActivity() {

    lateinit var data: DataNewsFeed
    private var loadingDialog: LoadingDialog? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)

        //이전 Activity에서 뉴드 데이터 받아오기
        if (intent.hasExtra("EXTRA_NEWS_FEED")) {
            data = intent.getSerializableExtra("EXTRA_NEWS_FEED") as DataNewsFeed
            infoLog(data.toString())
        }

        //로딩창 시작
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
        }
        loadingDialog!!.progressON(this, "Loading...", true)

        setData()
        setWebview()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }

    //데이터 세팅
    private fun setData(){
        tv_title.text = data.title

        when {
            //키워드의 갯수에 따라 보여지는 뷰 분기
            data.keywords.size == 0 -> kl_keyword.visibility = View.GONE
            data.keywords.size == 1 -> {
                kl_keyword.visibility = View.VISIBLE
                kl_keyword.addKeyWord(data.keywords[0])
            }
            data.keywords.size == 2 -> {
                kl_keyword.visibility = View.VISIBLE
                kl_keyword.addKeyWord(data.keywords[0], data.keywords[1])
            }
            data.keywords.size >= 3 -> {
                kl_keyword.visibility = View.VISIBLE
                kl_keyword.addKeyWord(data.keywords[0], data.keywords[1], data.keywords[2])
            }
        }
    }

    //웹뷰 세팅
    private fun setWebview(){
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

        //웹뷰가 시작한 시점
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

        }

        //웹뷰가 다 그려진 시점
        override fun onPageFinished(view: WebView?, url: String?) {
            loadingDialog!!.progressOFF(500)
        }
    }
}
