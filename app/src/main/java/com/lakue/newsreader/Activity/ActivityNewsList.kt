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
import kotlin.collections.HashMap
import android.annotation.SuppressLint
import android.widget.Toast
import com.lakue.newsreader.Base.*
import com.lakue.newsreader.Listener.OnScrollViewStateListener
import com.lakue.newsreader.Type.CustomScrollView
import java.io.IOException
import kotlin.coroutines.coroutineContext


class ActivityNewsList : BaseActivity() {

    companion object {
        private var startItem = 0
        private var maxItemCount = 0
        private const val SHOWITEMCOUNT = 7
        private var endItem = 0
        private var isLoading = false
        private var isLastPage = false

        var arrFeed: ArrayList<DataNewsFeed> = ArrayList()
        @SuppressLint("StaticFieldLeak")
        lateinit var adapter: AdapterRecyclerView
        private var loadingDialog: LoadingDialog? = null

    }

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

        //rss parsing
        val v = OKXmlParser()
        v.execute(url)

        //당겨서 새로고침
        swipe_refresh.setOnRefreshListener {
            setRefresh(url)
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
        swipe_refresh.isRefreshing = false

        val v = OKXmlParser()
        v.execute(url)

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
                    startItem += SHOWITEMCOUNT
                    val v = JsoupAsyncTask()
                    v.execute(arrFeed)
                }
            }

        })
    }

    //RSS parse 데이터 가져오기
    class OKXmlParser :
        AsyncTask<String?, Void?, Void?>() {
        override fun doInBackground(vararg params: String?): Void? {
            val url: URL
            var document: Document?

            try {
                url = URL(params[0])
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
                Log.i("ActivityNewsListTAG", "OKXmlParser arrFeed.size : " + arrFeed.size)
                maxItemCount = arrFeed.size
            } catch (e: Exception) {
                Log.e("OKXmlParserException", e.toString())
            }
            return null
        }

        //비동기처리가 완료되었을 때 실행
        override fun onPostExecute(result: Void?) {
            Log.i("ActivityNewsListTAG", "OKXmlParser startItem : $startItem")
            Log.i("ActivityNewsListTAG", "OKXmlParser endItem : $endItem")
            Log.i("ActivityNewsListTAG", "OKXmlParser maxItemCount : $maxItemCount")
            Log.i("ActivityNewsListTAG", "OKXmlParser isLoading : $isLoading")
            Log.i("ActivityNewsListTAG", "OKXmlParser isLastPage : $isLastPage")
            val v = JsoupAsyncTask()
            v.execute(arrFeed)
        }
    }

    @SuppressLint("StaticFieldLeak")
    class JsoupAsyncTask :
        AsyncTask<ArrayList<DataNewsFeed>, Void, ArrayList<DataNewsFeed>>() {
        override fun doInBackground(vararg params: ArrayList<DataNewsFeed>?): ArrayList<DataNewsFeed>? {
            try {
                endItem = startItem + SHOWITEMCOUNT
                if(endItem > maxItemCount){
                    endItem = maxItemCount
                    isLastPage = true
                }

                for (i in startItem until endItem) {
                    Log.e("QRWKJKL",params[0]!![i].title)
                    var image = ""
                    var description = ""
                    var keywords: ArrayList<String> = ArrayList()

                    val con = Jsoup.connect(params[0]!![i].link)
                    val doc = con.get()
                    val ogTags = doc.select("meta[property^=og:]")
                    if (ogTags.size <= 0) {
                        params[0]!![i].description = description
                        params[0]!![i].image = image
                        params[0]!![i].keywords = keywords
                        continue
                    }

                    for (indice in ogTags.indices) {
                        val tag = ogTags[indice]
                        val text = tag.attr("property")
                        if ("og:image" == text) {
                            image = tag.attr("content")
                        } else if ("og:description" == text) {
                            description = tag.attr("content")
                        }
                    }

                    if (description.isEmpty()) {
                        description = params[0]!![i].title
                    }

                    //키워드 가져오기
                    keywords = ExtractKeyWord.getKeyWords(description)

                    params[0]!![i].description = description
                    params[0]!![i].image = image
                    params[0]!![i].keywords = keywords
                }
                return params[0]

            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: java.lang.Exception){
                e.printStackTrace()
            }

            return null
        }

        // doInBackground 작업 후 작업
        override fun onPostExecute(arrFeed: ArrayList<DataNewsFeed>) {
            adapter.removeLoading()
            Log.i("ActivityNewsListTAG", "JsoupAsyncTask startItem : $startItem")
            Log.i("ActivityNewsListTAG", "JsoupAsyncTask endItem : $endItem")
            Log.i("ActivityNewsListTAG", "JsoupAsyncTask maxItemCount : $maxItemCount")
            Log.i("ActivityNewsListTAG", "JsoupAsyncTask isLoading : $isLoading")
            Log.i("ActivityNewsListTAG", "JsoupAsyncTask isLastPage : $isLastPage")

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
        }
    }
}
