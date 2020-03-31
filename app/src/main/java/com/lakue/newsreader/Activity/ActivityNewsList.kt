package com.lakue.newsreader.Activity

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.lakue.newsreader.*
import com.lakue.newsreader.Adapter.AdapterRecyclerView
import com.lakue.newsreader.Base.BaseActivity
import com.lakue.newsreader.Base.LoadingDialog
import com.lakue.newsreader.Base.SSLConnect
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
import com.lakue.newsreader.Listener.OnScrollViewStateListener
import com.lakue.newsreader.Type.CustomScrollView
import java.io.IOException


class ActivityNewsList : BaseActivity() {

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
            infoLog("Refresh")
            adapter.reset()
            startItem = 0
            maxItemCount = 0
            endItem = 0
            isLastPage = false
            val v = OKXmlParser()
            v.execute(url)
            loadingDialog!!.progressON(this, "Loading...", true)
            swipe_refresh.isRefreshing = false
        }
    }

    //RecyclerView 세팅
    fun setRecyclerViewInit() {
        val linearlayoutManager = LinearLayoutManager(this)
        rv_news_feed.layoutManager = linearlayoutManager
        rv_news_feed.setHasFixedSize(true)
        rv_news_feed.setItemViewCacheSize(20)
//        rv_news_feed.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
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
                if(!isLoading && !isLastPage){
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
                    var image = ""
                    var description = ""
                    var keywords: ArrayList<String> = ArrayList()

                    //데이터 저장
                    var dataNewsFeed =
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
                maxItemCount = arrFeed.size
            } catch (e: Exception) {
                Log.e("OKXmlParserException", e.toString())
//                Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show()
            }
            return null
        }

        //비동기처리가 완료되었을 때 실행
        override fun onPostExecute(result: Void?) {
            val v = JsoupAsyncTask()
            v.execute(arrFeed)

//            for (item in arrFeed!!) {
//                adapter.addItem(item)
//            }
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
                    keywords = getKeyWords(description)

                    params[0]!![i].description = description
                    params[0]!![i].image = image
                    params[0]!![i].keywords = keywords
                }
                return params[0]

            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }


        override fun onPostExecute(arrFeed: ArrayList<DataNewsFeed>) { // doInBackground 작업 후 작업
            for(i in startItem until endItem){
                adapter.addItem(arrFeed[i])
            }
            isLoading = false
        }
    }

    companion object {
        private var startItem = 0
        private var maxItemCount = 0
        private val SHOWITEMCOUNT = 6
        private var endItem = 0
        private var isLoading = false
        private var isLastPage = false

        var arrFeed: ArrayList<DataNewsFeed> = ArrayList()
        lateinit var adapter: AdapterRecyclerView
        private var loadingDialog: LoadingDialog? = null
        //        }
        fun getKeyWords(description: String): ArrayList<String> {
            val removeSpecialCharacters: String =
                SpecialCharactersRreplace(description)
            var feedKeyword: ArrayList<String> = ArrayList()
            //띄어쓰기마다 split
            val keywords: ArrayList<String> =
                removeSpecialCharacters.split(" ") as ArrayList<String>
            val keywordMap: HashMap<String, Int> = HashMap()

            var isKeyWordEquare = false
            for (keyword in keywords) {
                var trimKeyword = keyword.trim()
                //키워드 글자수가 2글자 이상인지 판단
                if (trimKeyword.length < 2)
                    continue

                //처음 해쉬맵안에 데이터가 비어있으면 키워드(key)와 갯수(value:1)지정
                if (keywordMap.isEmpty()) {
                    keywordMap.set(trimKeyword, 1)
                } else {
                    //해쉬맵 안에 있는 키워드일 경우 value+1을 해주고,
                    //없을 경우 키워드(key)와 갯수(value:1)지정
                    for (key in keywordMap) {
                        if (key.key == trimKeyword) {
                            var count = keywordMap.get(trimKeyword)
                            count = count?.plus(1)
                            keywordMap.set(trimKeyword, count!!)
                            isKeyWordEquare = true
                            break
                        } else {
                            isKeyWordEquare = false
                        }
                    }
                    if (!isKeyWordEquare) {
                        keywordMap.set(trimKeyword, 1)
                    }
                }
            }

            //받아온 키워드를 키값으로 오름차순 정렬(숫자,영어,ㄱ,ㄴ,ㄷ순)
            val result = keywordMap.toList().sortedBy { (key, _) -> key }.toMap()

            //정렬한 키워드에서 다시 갯수대로 내림차순 정렬(가장 많이 사용한 키워드가 맨 앞으로 오도록)
            val result1 = result.toList().sortedByDescending { (_, value) -> value }.toMap()

            Log.e("sortKeyword", result1.toString())
            feedKeyword = ArrayList(result1.keys)

            return feedKeyword
        }

        //특수문자, 개행 제거
        fun SpecialCharactersRreplace(str: String): String {
            var str = str
            val match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]"
            str = str.replace(match.toRegex(), "")
            str = str.replace("\n", "")
            str = str.replace("\t", " ")
            return str
        }
    }


}
