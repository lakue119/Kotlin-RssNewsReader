package com.lakue.newsreader.Activity

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.lakue.newsreader.*
import com.lakue.newsreader.Base.BaseActivity
import com.lakue.newsreader.Base.LoadingDialog
import com.lakue.newsreader.Base.SSLConnect
import com.lakue.newsreader.Listener.OnRecyclerViewCompleteListener
import kotlinx.android.synthetic.main.activity_news_list.*
import org.jsoup.Jsoup
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ActivityNewsList : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)

        var url = "https://news.google.com/rss?hl=ko&gl=KR&ceid=KR:ko"

        setRecyclerViewInit()

        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
        }
        loadingDialog!!.progressON(this, "Loading...", true)

        var sslConnect = SSLConnect()
        sslConnect.postHttps(url, 1000, 1000)
        val v = OKXmlParser()
        v.execute(url)

        swipe_refresh.setOnRefreshListener {
            adapter.reset()
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

        adapter = AdapterRecyclerView(this, RecyclerViewType.NEWS_FEED)

        rv_news_feed.adapter = adapter

        adapter.setOnRecyclerViewCompleteListener(object :OnRecyclerViewCompleteListener{
            override fun onRecyclerViewComplete() {
                loadingDialog!!.progressOFF(500)
            }
        })
    }

    //RSS parse 데이터 가져오기
    class OKXmlParser :
        AsyncTask<String?, Void?, ArrayList<DataNewsFeed>?>() {
        override fun doInBackground(vararg params: String?): ArrayList<DataNewsFeed>? {
            val url: URL
            var document: Document?
            var arrFeed: ArrayList<DataNewsFeed> = ArrayList()

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
                    val descriptionNodeList = element.getElementsByTagName("description")
                    val title = titleNodeList.item(0).childNodes.item(0).nodeValue
                    val link = linkNodeList.item(0).childNodes.item(0).nodeValue
                    val descriptionrss = descriptionNodeList.item(0).childNodes.item(0).nodeValue
                    var image = ""
                    var description = ""
                    var keywords: ArrayList<String> = ArrayList()

                    //link본문에 있는 meta property image와 description 추출
                    val con = Jsoup.connect(link).ignoreHttpErrors(true)
                    val doc = con.get()
                    val ogTags = doc.select("meta[property^=og:]")
                    if (ogTags.size <= 0) {
                        var dataNewsFeed =
                            DataNewsFeed(i.toString(), title, link, image, description, keywords)
                        arrFeed.add(dataNewsFeed)
                        continue
                    }

                    for (indice in ogTags.indices) {
                        val tag = ogTags[indice]
                        val text = tag.attr("property")
                        if ("og:image" == text) {
                            var getImage = tag.attr("content")
                            image = getImage
                        } else if ("og:description" == text) {
                            var getDescription = tag.attr("content")
                            description = getDescription
                            if(description.isEmpty()){
                                description = title
                            }
                            keywords = getKeyWords(description)
                        }
                    }

                    Log.e("title", title)
                    Log.e("link", link)
                    Log.e("image", image)
                    Log.e("descriptionrss", descriptionrss)
                    Log.e("description", description)
                    Log.e("keywords", keywords.toString())

                    var dataNewsFeed =
                        DataNewsFeed(i.toString(), title, link, image, description, keywords)
                    arrFeed.add(dataNewsFeed)


                }
                return arrFeed
            } catch (e: Exception) {
                Log.e("QLKWRJLWQKR", e.toString())
//                Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show()
            }
            return null
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(arrFeed: ArrayList<DataNewsFeed>?) {
            super.onPostExecute(arrFeed)
            for (item in arrFeed!!) {
                adapter.addItem(item)
            }


            //Log.e("QLKJRWKLQWJRKL", arrFeed.toString())
        }

        override fun onProgressUpdate(vararg values: Void?) {
            super.onProgressUpdate(*values)
        }
    }

    companion object {
        lateinit var adapter: AdapterRecyclerView
        private var loadingDialog: LoadingDialog? = null
        //        fun isFirstCharIsNum(str: String): Boolean{
//            val firstChar = str.substring(0,1)
//            val reg1 = Regex("^[0-9]+$")
//            return firstChar.matches(reg1)
//        }
        fun getKeyWords(description: String): ArrayList<String> {
            val removeSpecialCharacters: String =
                SpecialCharactersRreplace(description)
            var feedKeyword: ArrayList<String> = ArrayList()
            val keywords: ArrayList<String> = removeSpecialCharacters.split(" ") as ArrayList<String>
            val keywordMap: HashMap<String, Int> = HashMap()
            val dumy: String

            var isKeyWordEquare = false
            for (keyword in keywords) {
                var trimKeyword = keyword.trim()
                if (trimKeyword.length < 2)
                    continue
                if (keywordMap.isEmpty()) {
                    keywordMap.set(trimKeyword, 1)
                } else {
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

            val result = keywordMap.toList().sortedBy { (key, value) -> key }.toMap()
            val result1 = result.toList().sortedByDescending { (key, value) -> value }.toMap()

            Log.e("sortKeyword", result1.toString())
            feedKeyword = ArrayList(result1.keys)

            return feedKeyword
        }

        fun SpecialCharactersRreplace(str: String): String {
            var str = str
            val match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]"
            str = str.replace(match.toRegex(), "")
            //str = str.replace("\n","")
            return str
        }
    }


}
