package com.lakue.newsreader

import com.lakue.newsreader.Module.MyItem

data class DataRssFeed(val title:String, val description: String, val link: String, val pubdate: String, val itemList: List<DataRss>): MyItem() {

}