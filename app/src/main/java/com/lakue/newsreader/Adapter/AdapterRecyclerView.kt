package com.lakue.newsreader.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lakue.newsreader.Base.MyItem
import com.lakue.newsreader.Base.MyItemView
import com.lakue.newsreader.Listener.OnRecyclerViewCompleteListener
import com.lakue.newsreader.R
import com.lakue.newsreader.Type.RecyclerViewType
import com.lakue.newsreader.ViewHolder.ProgressHolder
import com.lakue.newsreader.ViewHolder.ViewHolderNewsReader

class AdapterRecyclerView(val context: Context, private val type: RecyclerViewType) :
    RecyclerView.Adapter<MyItemView>() {

    private var myItems = ArrayList<MyItem>()
    private var isLoaderVisible = false //로딩상태인지 체크

    private val VIEW_TYPE_LOADING = 0
    private val VIEW_TYPE_NORMAL = 1

    private lateinit var onRecyclerViewCompleteListener: OnRecyclerViewCompleteListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyItemView {
        lateinit var holder: MyItemView

        var view: View

        //여러곳에서 RecyclerView를 호출할때마다 새로 생성하지 않고 아답터를 불러오는 방식으로 Type을 지정
        if (viewType == VIEW_TYPE_LOADING) run {
            view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_loading, parent, false)
            holder = ProgressHolder(view)
        } else if (type == RecyclerViewType.NEWS_FEED) {
            view = LayoutInflater.from(context)
                .inflate(R.layout.item_news_feed, parent, false)
            holder = ViewHolderNewsReader(view)
        }
        return holder
    }

    // RecyclerView의 총 개수
    override fun getItemCount(): Int {
        return myItems.size
    }

    override fun onBindViewHolder(holder: MyItemView, position: Int) {
        if (holder is ProgressHolder) run {
            onRecyclerViewCompleteListener.onRecyclerViewComplete()

        } else if (holder is ViewHolderNewsReader) {
            if (position == itemCount - 1) {
                onRecyclerViewCompleteListener.onRecyclerViewComplete()
            }
            holder.onBind(myItems[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            if (position == myItems.size - 1) VIEW_TYPE_LOADING else VIEW_TYPE_NORMAL
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    //아답터 하단 로딩화면 추가
    fun addLoading() {
        isLoaderVisible = true
        myItems.add(MyItem())
        notifyItemInserted(myItems.size - 1)
    }

    //아답터 하단 로딩화면 제거
    fun removeLoading() {
        if (myItems.size > 0) {
            isLoaderVisible = false
            val position = myItems.size - 1
            myItems.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    //데이터 추가
    fun addItem(data: MyItem) {
        myItems.add(data)
        notifyItemInserted(myItems.size - 1)
    }

    //데이터 초기화
    fun reset() {
        myItems.clear()
        notifyDataSetChanged()
    }

    fun setOnRecyclerViewCompleteListener(onRecyclerViewCompleteListener: OnRecyclerViewCompleteListener) {
        this.onRecyclerViewCompleteListener = onRecyclerViewCompleteListener
    }
}