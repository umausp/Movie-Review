package com.app.data.repository

import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.app.data.network.MovieReviewApiService
import com.app.data.paging.MovieReviewPagingSource
import com.app.data.storage.MovieReviewDataBase
import com.app.data.storage.MovieReviewEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

private const val NETWORK_PAGE_SIZE = 20

@ExperimentalPagingApi
class MovieReviewRepository @Inject constructor(
    private val movieReviewApiService: MovieReviewApiService,
    private val movieReviewDb: MovieReviewDataBase,
) {

    @WorkerThread
    fun getAllMovies(): Flow<PagingData<MovieReviewEntity>> = Pager(
        config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
        remoteMediator = MovieReviewPagingSource(movieReviewApiService, movieReviewDb)
    ) {
        movieReviewDb.getMovieReviewDao().moviesPagingSource()

    }.flow

}
