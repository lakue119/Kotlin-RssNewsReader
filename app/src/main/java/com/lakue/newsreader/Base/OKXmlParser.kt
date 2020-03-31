package com.lakue.newsreader.Base

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import org.jsoup.Jsoup
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class OKXmlParser(var mContext: Context) :
    AsyncTask<String?, Void?, Document?>() {

    override fun doInBackground(vararg params: String?): Document? {
        val url: URL
        var document: Document? = null
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
                Log.e("title", title)
                Log.e("link", link)

                //link본문에 있는 meta property image와 description 추출
                val con = Jsoup.connect(link)
                val doc = con.get()
                val ogTags = doc.select("meta[property^=og:]")
                if (ogTags.size <= 0) {
                    return null
                }

                for (i in ogTags.indices) {
                    val tag = ogTags[i]
                    val text = tag.attr("property")
                    if ("og:image" == text) {
                        var get_Image = tag.attr("content")
                        Log.e("QLKWRJLWQKR","image : $get_Image")
                    } else if ("og:description" == text) {
                        var get_Description = tag.attr("content")
                        Log.e("QLKWRJLWQKR","description : $get_Description")
                    }
                }
            }

        } catch (e: Exception) {
            Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show()
        }
        return document
    }

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(doc: Document?) {
        super.onPostExecute(doc)
    }

    override fun onProgressUpdate(vararg values: Void?) {
        super.onProgressUpdate(*values)
    }

}