package com.lakue.newsreader.Activity

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.lakue.newsreader.AdapterRecyclerView
import com.lakue.newsreader.DataNewsFeed
import com.lakue.newsreader.Module.ModuleActivity
import com.lakue.newsreader.R
import com.lakue.newsreader.RecyclerViewType
import kotlinx.android.synthetic.main.activity_main.*
import org.xml.sax.Attributes
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import org.xml.sax.XMLReader
import org.xml.sax.helpers.DefaultHandler
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory


class ActivityMain : ModuleActivity() {

    lateinit var adapter: AdapterRecyclerView
    var streamTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setRecyclerViewInit()

        val xmlTask = ProcessXmlTask()
        xmlTask.execute("https://news.google.com/rss?hl=ko&gl=KR&ceid=KR:ko")
    }

    //RecyclerView 세팅
    fun setRecyclerViewInit(){
        val linearlayoutManager= LinearLayoutManager(this)
        rv_news_feed.layoutManager = linearlayoutManager

        adapter = AdapterRecyclerView(this, RecyclerViewType.NEWS_FEED)

        rv_news_feed.adapter = adapter

        var keywords: ArrayList<String> = ArrayList()
        keywords.add("하이")
        keywords.add("할룽")
        keywords.add("ㅎ호")

        var data1 = DataNewsFeed("1","rrrr","https://taegon.kim/wp-content/uploads/2018/05/image-5.png","GJKL",keywords)
        adapter.addItem(data1)
        var data2 = DataNewsFeed("2","2222222222","https://taegon.kim/wp-content/uploads/2018/05/image-5.png","adsfasdfa",keywords)
        adapter.addItem(data2)
        var data3 = DataNewsFeed("3","3333333333","https://taegon.kim/wp-content/uploads/2018/05/image-5.png","zfzxcv",keywords)
        adapter.addItem(data3)
        var data4 = DataNewsFeed("4","4444444444444444444","https://taegon.kim/wp-content/uploads/2018/05/image-5.png","23623467",keywords)
        adapter.addItem(data4)
        var data5 = DataNewsFeed("5","5555555555555555555","https://taegon.kim/wp-content/uploads/2018/05/image-5.png","hgflghjlo",keywords)
        adapter.addItem(data5)
    }

    // AsyncTask<Params,Progress,Result>
    private class ProcessXmlTask :
        AsyncTask<String?, Void?, Void?>() {
        override fun doInBackground(vararg urls: String?): Void? {
            try {
                val rssUrl = URL(urls[0])
                val mySAXParserFactory: SAXParserFactory = SAXParserFactory.newInstance()
                val mySAXParser: SAXParser = mySAXParserFactory.newSAXParser()
                val myXMLReader: XMLReader = mySAXParser.getXMLReader()
                val myRSSHandler = RSSHandler()
                myXMLReader.setContentHandler(myRSSHandler)
                val myInputSource = InputSource(rssUrl.openStream())
                myXMLReader.parse(myInputSource)

                Log.i("QRQWRWQRQQE","strTitle" + myRSSHandler.strTitle)
                Log.i("QRQWRWQRQQE","strElement" + myRSSHandler.strElement)

            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: ParserConfigurationException) {
                e.printStackTrace()
            } catch (e: SAXException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
        }
    }

    private class RSSHandler : DefaultHandler() {
        val stateUnknown = 0
        val stateTitle = 1
        var state = stateUnknown
        var numberOfTitle = 0
        var strTitle = ""
        var strElement = ""
        @Throws(SAXException::class)
        override fun startDocument() {
            strTitle = "--- Start Document ---\n"
        }

        @Throws(SAXException::class)
        override fun endDocument() {
            strTitle += "--- End Document ---"
        }

        @Throws(SAXException::class)
        override fun startElement(
            uri: String?,
            localName: String,
            qName: String?,
            attributes: Attributes?
        ) {
            if (localName.equals("title", ignoreCase = true)) {
                state = stateTitle
                strElement = "Title: "
                numberOfTitle++
            } else {
                state = stateUnknown
            }
        }

        @Throws(SAXException::class)
        override fun endElement(
            uri: String?,
            localName: String,
            qName: String?
        ) {
            if (localName.equals("title", ignoreCase = true)) {
                strTitle += strElement + "\n"
            }
            state = stateUnknown
        }

        @Throws(SAXException::class)
        override fun characters(ch: CharArray?, start: Int, length: Int) {
            val strCharacters = String(ch!!, start, length)
            if (state == stateTitle) {
                strElement += strCharacters
            }
        }
    }
}
