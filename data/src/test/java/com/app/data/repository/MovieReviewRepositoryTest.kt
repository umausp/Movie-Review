package com.app.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.map
import com.app.data.model.MovieReviewModel
import com.app.data.network.MovieReviewApiService
import com.app.data.storage.MovieReviewEntity
import io.kotlintest.matchers.shouldBe
import io.kotlintest.mock.mock
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class MovieReviewRepositoryTest {

    @ExperimentalPagingApi
    lateinit var movieReviewRepository: ReposRepositoryImplFake

    private val movieApiService = mockk<MovieReviewApiService>(relaxed = true)

    @ExperimentalPagingApi
    @Before
    fun setup() {
        movieReviewRepository = ReposRepositoryImplFake()
    }

    @ExperimentalPagingApi
    @Test
    fun `get movie list with flow`() = runBlocking {

        val remoteDataSource = movieReviewRepository.getAllMovies()
        remoteDataSource.collect { paged : PagingData<MovieReviewEntity> ->
            paged.map {
                it.shouldBe(MovieReviewEntity::class.java)
            }
        }
    }

}
