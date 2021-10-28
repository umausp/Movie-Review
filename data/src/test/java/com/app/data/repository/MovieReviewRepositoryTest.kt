package com.app.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.app.data.storage.MovieReviewEntity
import com.app.data.utils.launchOnLifecycleScope
import com.google.common.truth.ExpectFailure.assertThat
import io.mockk.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Test

class MovieReviewRepositoryTest {

    @ExperimentalPagingApi
    val movieReviewRepository = mockk<ReposRepositoryImplFake>()

    @ExperimentalPagingApi
    @Test
    fun `get movie list with flow`() {

        coEvery { movieReviewRepository.getAllMovies() } returns flow {
            emit(PagingData.from(movieReviewRepository.listReposEntity))
        }

//        assertThat()
    }
}
