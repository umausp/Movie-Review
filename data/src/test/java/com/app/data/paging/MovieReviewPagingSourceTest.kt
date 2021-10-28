package com.app.data.paging

import androidx.paging.*
import com.app.data.network.MovieReviewApiService
import com.app.data.storage.MovieReviewDataBase
import com.app.data.storage.MovieReviewEntity
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalPagingApi
@OptIn(ExperimentalCoroutinesApi::class)
class MovieReviewPagingSourceTest {

    @MockK
    private lateinit var movieApiService: MovieReviewApiService

    private lateinit var moviePagingSource: MovieReviewPagingSource

    @MockK
    lateinit var movieDaoMock: MovieReviewDataBase

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        moviePagingSource = MovieReviewPagingSource(movieApiService, movieDaoMock)
    }

    @Test
    fun refreshLoadSuccessAndEndOfPaginationWhenNoMoreData() = runBlocking {

        val ioException = IOException("Some message for exception")

        coEvery {
            movieApiService.getMovieReviews(0)
        } throws ioException
        val pagingState = PagingState<Int, MovieReviewEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = moviePagingSource.load(LoadType.REFRESH, pagingState)
        assertThat(
            result
        ).isInstanceOf(RemoteMediator.MediatorResult.Error::class.java)
    }

    @After
    fun teardown() {
        unmockkAll()
    }

}