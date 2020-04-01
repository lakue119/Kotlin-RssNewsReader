package com.lakue.newsreader.ViewHolder

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lakue.newsreader.Activity.ActivityNewsDetail
import com.lakue.newsreader.Data.DataNewsFeed
import com.lakue.newsreader.Base.MyItem
import com.lakue.newsreader.Base.MyItemView
import com.lakue.newsreader.R
import com.lakue.newsreader.View.KeywordLayout
import com.lakue.newsreader.View.RoundedImageView
import org.apache.commons.io.IOUtils
import java.net.URL

class ViewHolderNewsReader(itemView: View) : MyItemView(itemView)  {

    private lateinit var data: DataNewsFeed
    private var rivNewsImage = itemView.findViewById<RoundedImageView>(R.id.riv_news_image)
    private var tvNewsTitle = itemView.findViewById<TextView>(R.id.tv_news_title)
    private var tvNewsContent = itemView.findViewById<TextView>(R.id.tv_news_content)
    private var klKeyword = itemView.findViewById<KeywordLayout>(R.id.kl_keyword)
    private var llFeed = itemView.findViewById<LinearLayout>(R.id.ll_feed)

    fun onBind(item: MyItem){
        this.data = item as DataNewsFeed

        ImageCheckTask().execute(data.image)

        tvNewsTitle.text = data.title
        tvNewsContent.text = data.description

        Log.i("ViewHolderNewsReader","title" + data.title)
        Log.i("ViewHolderNewsReader","description" + data.description)
        Log.i("ViewHolderNewsReader","keywords" + data.keywords.toString())
        Log.i("ViewHolderNewsReader","klKeyword.count" + klKeyword.count)

        klKeyword.reset()
        when {
            data.keywords.size == 0 -> klKeyword.visibility = View.GONE
            data.keywords.size == 1 -> {
                klKeyword.visibility = View.VISIBLE
                klKeyword.addKeyWord(data.keywords[0])
            }
            data.keywords.size == 2 -> {
                klKeyword.visibility = View.VISIBLE
                klKeyword.addKeyWord(data.keywords[0],data.keywords[1])
            }
            data.keywords.size >= 3 -> {
                klKeyword.visibility = View.VISIBLE
                klKeyword.addKeyWord(data.keywords[0],data.keywords[1],data.keywords[2])
            }
        }

        //뉴스피드 클릭 시 상세화면 이동
        llFeed.setOnClickListener {
            var intent = Intent(itemView.context,ActivityNewsDetail::class.java)
            intent.putExtra("EXTRA_NEWS_FEED",data)
            itemView.context.startActivity(intent)

        }
    }

    //네트워크에서 이미지URL을 판단하고 byte[]타입으로 변환하는 쓰레드
    @SuppressLint("StaticFieldLeak")
    private inner class ImageCheckTask : AsyncTask<String, Void, Boolean>() {
        override fun doInBackground(vararg links: String): Boolean {
            try {
                val url = URL(links[0])
                val inputStream = url.openStream()
                val imageBytes = IOUtils.toByteArray(inputStream)

                return isImage(imageBytes)

            }catch (e: Exception) {
                //http식으로 안형들어왔을 경우 false 리턴
                return false
            }

        }

        override fun onPostExecute(isImage: Boolean) {
            if(isImage){
                rivNewsImage.visibility = View.VISIBLE
                Glide.with(itemView.context).load(data.image).placeholder(R.drawable.loading).into(rivNewsImage)
            } else{
                rivNewsImage.visibility = View.GONE
            }
        }
    }

    private fun isImage(data: ByteArray): Boolean {
        val img = BitmapFactory.decodeByteArray(data, 0, data.size)
        return img != null
    }
}