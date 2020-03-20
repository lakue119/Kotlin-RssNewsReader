package com.lakue.newsreader

import com.lakue.newsreader.Module.MyItem

data class DataNewsFeed(val id: String, val title: String, val image: String, val content: String, val keywords: ArrayList<String>): MyItem() {

}