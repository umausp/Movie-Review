package com.app.moviereview.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.app.data.storage.MovieReviewEntity
import com.app.moviereview.MainActivity
import com.app.moviereview.R
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ImageUtilsKtTest {


    @get: Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    private lateinit var fakeMovieReviewEntity: List<MovieReviewEntity>

    @Before
    fun setUp() {
        fakeMovieReviewEntity = createListRepoEntity()
    }

    @Test
    fun bindImageUrlWithImage() {

        onView(withId(R.id.rv_movie_review)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_movie_review))
            .check(matches(withViewAtPosition(1, hasDescendant(allOf(withId(R.id.img_placeholder), isDisplayed())))));
    }

    private fun createListRepoEntity(): List<MovieReviewEntity> {
        return mutableListOf(
            MovieReviewEntity(
                title = "Groot"
            ),
            MovieReviewEntity(
                title = "Clint"
            ),
            MovieReviewEntity(
                title = "Hulk"
            ),
            MovieReviewEntity(
                title = "Avengers"
            )
        )
    }

    fun withViewAtPosition(position: Int, itemMatcher: Matcher<View?>): Matcher<View?>? {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) {
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                return viewHolder != null && itemMatcher.matches(viewHolder.itemView)
            }
        }
    }
}