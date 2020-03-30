package com.lakue.newsreader

import com.lakue.newsreader.Base.MyItem

data class DataNewsFeed(var id: String,
                        var title: String, var link:String, var image: String, var description: String, var keywords: ArrayList<String>): MyItem() {

}