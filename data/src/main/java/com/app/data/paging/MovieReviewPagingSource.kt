package com.app.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.app.data.network.MovieReviewApiService
import com.app.data.storage.MovieReviewDataBase
import com.app.data.storage.MovieReviewEntity
import com.app.data.storage.RemoteKeys
import com.app.data.storage.toEntityData
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val MOVIE_REVIEW_STARTING_INDEX = 0

@OptIn(ExperimentalPagingApi::class)
class MovieReviewPagingSource @Inject constructor(
    private val movieReviewApiService: MovieReviewApiService,
    private val movieReviewDb: MovieReviewDataBase,
) : RemoteMediator<Int, MovieReviewEntity>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, MovieReviewEntity>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                MOVIE_REVIEW_STARTING_INDEX
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                val remoteKey: RemoteKeys? = movieReviewDb.withTransaction {
                    if (lastItem?.uId != null) {
                        movieReviewDb.remoteKeysDao().remoteKeysRepoId(lastItem.uId)
                    } else null
                }

                if (remoteKey?.hasMore == false) {
                    return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }

                remoteKey?.nextKey ?: MOVIE_REVIEW_STARTING_INDEX
            }
        }

        try {

            val response = movieReviewApiService.getMovieReviews(page)
            val endOfPaginationReached = response.has_more == false

            val currentMilliTime = System.currentTimeMillis()

            movieReviewDb.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    movieReviewDb.remoteKeysDao().clearRemoteKeys()
                    movieReviewDb.getMovieReviewDao().clearAllData()
                }
                val prevKey = if (page == MOVIE_REVIEW_STARTING_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val totalItem = page * 20
                val results = response.results?.mapIndexed { index, item ->
                    item.toEntityData(
                        pageOffset = nextKey ?: MOVIE_REVIEW_STARTING_INDEX,
                        currentMilliTime,
                        hasMore = response.has_more == true,
                        uId = index + totalItem
                    )
                }

                if (results != null) {

                    val keys = results.map {
                        RemoteKeys(repoId = it.uId, prevKey = prevKey, nextKey = nextKey, response.has_more)
                    }
                    movieReviewDb.remoteKeysDao().insertAll(keys)
                    movieReviewDb.getMovieReviewDao().insertAllMovieReviews(results)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }
}
