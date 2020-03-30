package com.lakue.newsreader.ViewHolder

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lakue.newsreader.Activity.ActivityNewsDetail
import com.lakue.newsreader.DataNewsFeed
import com.lakue.newsreader.Base.MyItem
import com.lakue.newsreader.Base.MyItemView
import com.lakue.newsreader.R
import com.lakue.newsreader.View.KeywordLayout
import com.lakue.newsreader.View.RoundedImageView

class ViewHolderNewsReader(itemView: View) : MyItemView(itemView)  {

    lateinit var data: DataNewsFeed

    fun onBind(item: MyItem){
        this.data = item as DataNewsFeed

        var rivNewsImage = itemView.findViewById<RoundedImageView>(R.id.riv_news_image)
        var tvNewsTitle = itemView.findViewById<TextView>(R.id.tv_news_title)
        var tvNewsContent = itemView.findViewById<TextView>(R.id.tv_news_content)
        var klKeyword = itemView.findViewById<KeywordLayout>(R.id.kl_keyword)
        var llFeed = itemView.findViewById<LinearLayout>(R.id.ll_feed)

        when{
            data.image.isEmpty() -> rivNewsImage.visibility = View.GONE
            data.image.isNotEmpty() -> {
                rivNewsImage.visibility = View.VISIBLE
                Glide.with(itemView.context).load(data.image).into(rivNewsImage)
            }
        }

        tvNewsTitle.text = data.title
        tvNewsContent.text = data.description

        Log.i("QWKJREKL","title" + data.title)
        Log.i("QWKJREKL","description" + data.description)
        Log.i("QWKJREKL","keywords" +
                data.keywords.toString())
        Log.i("QWKJREKL","klKeyword.count" + klKeyword.count)

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
            data.keywords.size > 3 -> {
                klKeyword.visibility = View.VISIBLE
                klKeyword.addKeyWord(data.keywords[0],data.keywords[1],data.keywords[2])
            }
        }


        llFeed.setOnClickListener {
            var intent = Intent(itemView.context,ActivityNewsDetail::class.java)
            intent.putExtra("EXTRA_NEWS_FEED",data)
            itemView.context.startActivity(intent)

        }
    }
}