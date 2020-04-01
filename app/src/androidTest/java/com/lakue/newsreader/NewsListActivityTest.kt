package com.lakue.newsreader

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.lakue.newsreader.Activity.ActivityNewsList
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class NewsListActivityTest {
    @get:Rule
    var mActivityRule: ActivityTestRule<ActivityNewsList> =
        ActivityTestRule(ActivityNewsList::class.java)
//    private var rv_news_feed: RecyclerView? = null
//    private var adapter: AdapterRecyclerView? = null
//    private var keywords: ArrayList<String> = ArrayList()

    @Before
    fun setUp() {
        Intents.init()

//        rv_news_feed = mActivityRule.activity.findViewById(R.id.rv_news_feed) as RecyclerView
//        adapter = AdapterRecyclerView(mActivityRule.activity, RecyclerViewType.NEWS_FEED)
//        rv_news_feed!!.adapter = adapter
//        keywords.add("삼성")
//        keywords.add("갤럭시")
//        keywords.add("20")
//        for (i in 0 until 6) {
//            val dataNewsFeed =
//                DataNewsFeed(
//                    i.toString(),
//                    "$i Title",
//                    "https://news.google.com/__i/rss/rd/articles/CBMiKWh0dHBzOi8vbS5zZWRhaWx5LmNvbS9OZXdzVmlldy8xWVhNUUsxVzgz0gEsaHR0cHM6Ly9tLnNlZGFpbHkuY29tL05ld3NWaWV3QW1wLzFZWE1RSzFXODM?oc=5",
//                    "https://newsimg.sedaily.com/2020/01/13/1YXMQK1W83_1.jpg",
//                    "삼성전자가 다음 달 공개할 예정인 차기 갤럭시 S시리즈 중 한 모델로 추정되는 사진(사진)이 유출됐다.해외 개발자 커뮤니티 XDA디벨로퍼는 ‘갤럭시S20플러스’의 실물 사진을 입수했다며 12일(현지시간) 공개했다..",
//                    keywords
//                )
//            adapter!!.addItem(dataNewsFeed)
//        }
    }

    @Test
    fun testEditTextInTextIsEmpty() {
        //button 을 클릭합니다.
        Espresso.onView(ViewMatchers.withId(R.id.tv_header_title))
            .perform(ViewActions.typeText("Title"))

        Espresso.onView(ViewMatchers.withId(R.id.tv_news_title))
            .check(ViewAssertions.matches(ViewMatchers.withText("Title")))
        Espresso.onView(ViewMatchers.withId(R.id.tv_news_content))
            .check(ViewAssertions.matches(ViewMatchers.withText("Content")))

    }

//    /**
//     * Waiting to load items
//     */
//    fun awaitItemLoad() {
//        Awaitility.await().until<Boolean> {
//            var finish = false
//            while (!finish) {
//                finish = mActivityRule?.let {
//                    !it.is
//                } ?: true
//            }
//            finish
//        }
//    }
}