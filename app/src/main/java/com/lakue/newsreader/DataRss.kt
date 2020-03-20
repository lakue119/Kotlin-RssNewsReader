package com.lakue.newsreader

import com.lakue.newsreader.Module.MyItem

data class DataRss(val title:String, val description: String, val link: String, val pubdate: String): MyItem() {

}