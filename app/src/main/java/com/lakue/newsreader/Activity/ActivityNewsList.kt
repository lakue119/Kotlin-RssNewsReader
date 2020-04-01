package com.lakue.newsreader.Activity

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.lakue.newsreader.*
import com.lakue.newsreader.Adapter.AdapterRecyclerView
import com.lakue.newsreader.Data.DataNewsFeed
import com.lakue.newsreader.Listener.OnRecyclerViewCompleteListener
import com.lakue.newsreader.Type.RecyclerViewType
import kotlinx.android.synthetic.main.activity_news_list.*
import org.jsoup.Jsoup
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.ArrayList
import android.annotation.SuppressLint
import com.lakue.newsreader.Base.*
import com.lakue.newsreader.Listener.OnScrollViewStateListener
import com.lakue.newsreader.Type.CustomScrollView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException


class ActivityNewsList : BaseActivity() {

    companion object {
        private const val SHOWITEMCOUNT = 7
    }

    private var startItem = 0
    private var maxItemCount = 0
    private var endItem = 0
    private var isLoading = false
    private var isLastPage = false
    private var isFinish = false


    var arrFeed: ArrayList<DataNewsFeed> = ArrayList()
    lateinit var adapter: AdapterRecyclerView
    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)


        //rss url 지정
        var url = "https://news.google.com/rss?hl=ko&gl=KR&ceid=KR:ko"

        //로딩창 생성
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
        }
        loadingDialog!!.progressON(this, "Loading...", true)

        //recyclerview 초기화
        setRecyclerViewInit()

        //ssl오류가 나도 계속 진행
        var sslConnect = SSLConnect()
        sslConnect.postHttps(url, 1000, 1000)

        RssListTask(url)

        //당겨서 새로고침
        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
            //데이터가 쌓이는 도중에 당겨서 새로고침 못하도록 예외처리
            if(isLoading){
                showToast("로딩이 끝나면 다시시도해주세요")
            } else{
                setRefresh(url)
            }

        }
    }

    //새로고침 기능수행 중 데이터 초기화
    private fun setRefresh(url: String){
        var itemCount =  arrFeed.size

        adapter.reset()
        startItem = 0
        maxItemCount = 0
        endItem = 0
        isLastPage = false
        arrFeed.clear()

        RssListTask(url)

        loadingDialog!!.progressON(this, "Loading...", true)

        infoLog("Refresh")
        infoLog("Refresh arrFeed.size : $itemCount")
    }

    //RecyclerView 세팅
    private fun setRecyclerViewInit() {
        val linearlayoutManager = LinearLayoutManager(this)
        rv_news_feed.layoutManager = linearlayoutManager
        rv_news_feed.setHasFixedSize(true)
        rv_news_feed.setItemViewCacheSize(20)
        adapter = AdapterRecyclerView(this, RecyclerViewType.NEWS_FEED)

        rv_news_feed.adapter = adapter

        adapter.setOnRecyclerViewCompleteListener(object : OnRecyclerViewCompleteListener {
            override fun onRecyclerViewComplete() {
                infoLog("onRecyclerViewComplete")
                loadingDialog!!.progressOFF(0)
            }
        })


        nsc_news_list.setOnScrollViewStateListener(object : OnScrollViewStateListener{
            override fun onScrollViewState(state: CustomScrollView) {
                infoLog("Bottom")
                if(!isLoading && !isLastPage){
                    infoLog("MoreData")
                    isLoading = true
                    startItem += Companion.SHOWITEMCOUNT
                    metaDataTask()
                }
            }

        })
    }

    //url에서 피드 리스트를 가져옴..
    //이미지,내용을 제외한 제목, 링크만 가져와서 저장
    private fun RssListTask(param: String) {
        CoroutineScope(Dispatchers.Main).launch {
            // Show progress from UI thread

            CoroutineScope(Dispatchers.Default).async {
                // background thread
                val url: URL
                val document: Document?
                try {
                    url = URL(param)
                    val dbf =
                        DocumentBuilderFactory.newInstance()
                    val db = dbf.newDocumentBuilder()
                    document = db.parse(InputSource(url.openStream()))
                    document.documentElement.normalize()

                    //item에 있는 title, link 가져오기
                    val itemNodeList = document!!.getElementsByTagName("item")
                    for (i in 0 until itemNodeList.length) {
                        val node = itemNodeList.item(i)
                        val element = node as Element
                        val titleNodeList = element.getElementsByTagName("title")
                        val linkNodeList = element.getElementsByTagName("link")
                        val title = titleNodeList.item(0).childNodes.item(0).nodeValue
                        val link = linkNodeList.item(0).childNodes.item(0).nodeValue
                        val keywords: ArrayList<String> = ArrayList()

                        //데이터 저장
                        val dataNewsFeed =
                            DataNewsFeed(
                                i.toString(),
                                title,
                                link,
                                "",
                                "",
                                keywords
                            )
                        arrFeed.add(dataNewsFeed)
                    }
                    infoLog("RssListTask arrFeed.size : " + arrFeed.size)
                    maxItemCount = arrFeed.size

                    metaDataTask()

                } catch (e: Exception) {
                    errorLog(e.toString())
                }

            }.await()
            // UI data update from UI thread
            // Hide Progress from UI thread
        }
    }

    //위에서 생성한 리스트에서 keyword, image, description 추가
    fun metaDataTask() {
        CoroutineScope(Dispatchers.Main).launch {
            // Show progress from UI thread

            CoroutineScope(Dispatchers.Default).async {
                // background thread
                endItem = startItem + Companion.SHOWITEMCOUNT
                if(endItem > maxItemCount){
                    endItem = maxItemCount
                    isLastPage = true
                }

                for (i in startItem until endItem) {
                    infoLog(arrFeed[i].title)
                    var image = ""
                    var description = ""
                    var keywords: ArrayList<String> = ArrayList()

                    val con = Jsoup.connect(arrFeed[i].link)
                    val doc = con.get()
                    val ogTags = doc.select("meta[property^=og:]")
                    if (ogTags.size > 0) {
                        for (indice in ogTags.indices) {
                            val tag = ogTags[indice]
                            val text = tag.attr("property")
                            if ("og:image" == text) {
                                image = tag.attr("content")
                            } else if ("og:description" == text) {
                                description = tag.attr("content")
                            }
                        }

                    }

                    if (description.isEmpty()) {
                        description = arrFeed[i].title
                    }

                    //키워드 가져오기
                    keywords = ExtractKeyWord.getKeyWords(description)

                    arrFeed[i].description = description
                    arrFeed[i].image = image
                    arrFeed[i].keywords = keywords

                }

            }.await()
            // UI data update from UI thread
            // Hide Progress from UI thread
            adapter.removeLoading()
            infoLog("metaDataTask startItem : $startItem")
            infoLog("metaDataTask endItem : $endItem")
            infoLog("metaDataTask maxItemCount : $maxItemCount")
            infoLog("metaDataTask isLoading : $isLoading")
            infoLog("metaDataTask isLastPage : $isLastPage")

            for(i in startItem until endItem){
                adapter.addItem(arrFeed[i])
            }

            if (endItem < maxItemCount) {
                adapter.addLoading()
            } else {
                if(endItem > 0){
                    isLastPage = true
                }
            }

            isLoading = false
            isFinish = true

        }
    }
}
