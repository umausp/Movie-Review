package com.app.moviereview.utils

import com.app.data.storage.MovieReviewEntity
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class AppUtilsTest {

    private lateinit var appUtils: AppUtils
    private lateinit var fakeMovieReviewEntity: List<MovieReviewEntity>

    @Before
    fun setUp() {
        appUtils = AppUtils()
        fakeMovieReviewEntity = ReposEntityProvider.createListRepoEntity()
    }

    @Test
    fun `check can filter with empty query string`() {
        val query = ""
        val result = appUtils.canStartFiltering(query)
        assertThat(result).isFalse()
    }

    @Test
    fun `check can filter without empty query string`() {
        val query = "test"
        val result = appUtils.canStartFiltering(query)
        assertThat(result).isTrue()
    }

    @Test
    fun sortMovieEntity() {
        val fakeMovieData = fakeMovieReviewEntity

        val result = appUtils.sortMovieEntity(fakeMovieData)

        val expectedTitle = "Avengers"

        assertThat(result[0].title).isEqualTo(expectedTitle)
    }
}