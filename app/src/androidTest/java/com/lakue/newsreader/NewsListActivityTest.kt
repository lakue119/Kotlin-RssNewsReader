package com.lakue.newsreader

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.lakue.newsreader.Activity.ActivityNewsList
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class NewsListActivityTest {
    @Rule
    var mActivityRule: ActivityTestRule<ActivityNewsList> = ActivityTestRule(ActivityNewsList::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @Test
    fun testEditTextInTextIsEmpty() {
        //button 을 클릭합니다.
        Espresso.onView(ViewMatchers.withId(R.id.tv_header_title)).perform(ViewActions.typeText("Title"))

        Espresso.onView(ViewMatchers.withId(R.id.tv_news_title))
            .check(ViewAssertions.matches(ViewMatchers.withText("Title")))
        Espresso.onView(ViewMatchers.withId(R.id.tv_news_content))
            .check(ViewAssertions.matches(ViewMatchers.withText("Content")))

    }

}