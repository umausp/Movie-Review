package com.app.data.paging

import androidx.paging.*
import com.app.data.model.MovieReviewModel
import com.app.data.network.MovieReviewApiService
import com.app.data.repository.ReposEntityProvider
import com.app.data.storage.MovieReviewDataBase
import com.app.data.storage.MovieReviewEntity
import io.mockk.*
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalPagingApi
@OptIn(ExperimentalCoroutinesApi::class)
class MovieReviewPagingSourceTest {

    private val movieApiService = mockk<MovieReviewApiService>(relaxed = true)

    private lateinit var moviePagingSource: MovieReviewPagingSource

    private val movieDaoMock = mockk<MovieReviewDataBase>(relaxed = true)

    private lateinit var pagingState: PagingState<Int, MovieReviewEntity>

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        pagingState = PagingState(listOf(), null, PagingConfig(10), 10)
        moviePagingSource = MovieReviewPagingSource(movieApiService, movieDaoMock)
    }

    @Test
    fun `when page loads with empty data then it will show error data`() = runBlocking {
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

        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    @After
    fun teardown() {
        unmockkAll()
    }



}