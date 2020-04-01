package com.lakue.newsreader.Activity


import android.os.Handler
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.lakue.newsreader.R
import junit.framework.Assert.assertTrue
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@LargeTest
@RunWith(AndroidJUnit4::class)
class ActivityNewsListTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(ActivityNewsList::class.java)

    @Before
    fun setUp() {

        Thread.sleep(10000)
    }

    @Test
    fun activityNewsListTest() {
        if(getRecyclerViewCount() <= 0){
            return
        }

        val linearLayout = onView(
            allOf(
                withId(R.id.ll_feed),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.rv_news_feed),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        linearLayout.perform(click())

        pressBack()
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    private fun getRecyclerViewCount(): Int {
        var rv_news_feed = mActivityTestRule.activity.findViewById(R.id.rv_news_feed) as RecyclerView
        return rv_news_feed.adapter!!.itemCount
    }
}
