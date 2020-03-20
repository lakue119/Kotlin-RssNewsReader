package com.lakue.newsreader.ViewHolder

import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lakue.newsreader.DataNewsFeed
import com.lakue.newsreader.Module.MyItem
import com.lakue.newsreader.Module.MyItemView
import com.lakue.newsreader.R
import com.lakue.newsreader.View.RoundedImageView

class ViewHolderNewsReader(itemView: View) : MyItemView(itemView)  {

    lateinit var data: DataNewsFeed

    fun onBind(item: MyItem){
        this.data = item as DataNewsFeed

        var riv_news_image = itemView.findViewById<RoundedImageView>(R.id.riv_news_image)
        var tv_news_title = itemView.findViewById<TextView>(R.id.tv_news_title)
        var tv_news_content = itemView.findViewById<TextView>(R.id.tv_news_content)
        var tv_news_keyword1 = itemView.findViewById<TextView>(R.id.tv_news_keyword1)
        var tv_news_keyword2 = itemView.findViewById<TextView>(R.id.tv_news_keyword2)
        var tv_news_keyword3 = itemView.findViewById<TextView>(R.id.tv_news_keyword3)

        Glide.with(itemView.context).load(data.image).into(riv_news_image)
        tv_news_title.text = data.title
        tv_news_content.text = data.content
        tv_news_keyword1.text = data.keywords[0]
        tv_news_keyword2.text = data.keywords[1]
        tv_news_keyword3.text = data.keywords[2]


    }
}