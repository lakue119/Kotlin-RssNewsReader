package com.lakue.newsreader

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lakue.newsreader.Module.MyItem
import com.lakue.newsreader.Module.MyItemView
import com.lakue.newsreader.ViewHolder.ViewHolderNewsReader

class AdapterRecyclerView(context: Context, type: RecyclerViewType) :
    RecyclerView.Adapter<MyItemView>() {

    var myItems = ArrayList<MyItem>()

    val type = type
    val context = context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyItemView {
        lateinit var  holder: MyItemView

        var view: View

        //여러곳에서 RecyclerView를 호출할때마다 새로 생성하지 않고 아답터를 불러오는 방식으로 Type을 지정
        if (type == RecyclerViewType.NEWS_FEED) {
            view = LayoutInflater.from(context)
                .inflate(R.layout.item_news_feed, parent, false)
            holder = ViewHolderNewsReader(view)
        }
        return holder
    }

    // RecyclerView의 총 개수
    override fun getItemCount(): Int {
        return 5
        //return if (myItems == null) 0 else myItems.size
    }

    override fun onBindViewHolder(holder: MyItemView, position: Int) {
        if (holder is ViewHolderNewsReader) {
            val viewHolderNewsReader = holder as ViewHolderNewsReader
            viewHolderNewsReader.onBind(myItems[position])
        }
    }

    open fun addItem(data: MyItem){
        myItems.add(data)
        notifyItemInserted(myItems.size-1)
    }
}