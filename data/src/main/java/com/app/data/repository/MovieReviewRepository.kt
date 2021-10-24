package com.app.data.repository

import androidx.annotation.WorkerThread
import com.app.data.model.MovieReviewItemModel
import com.app.data.network.MovieReviewApiService
import com.app.data.storage.MovieReviewDao
import com.app.data.storage.toEntityData
import com.app.data.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MovieReviewRepository @Inject constructor(
    private val movieReviewApiService: MovieReviewApiService,
    private val movieReviewDao: MovieReviewDao,
    private val ioDispatcher: CoroutineDispatcher
) {

    @WorkerThread
    fun getMovieReviews(
        offset: Int,
    ): Flow<Resource<List<MovieReviewItemModel>>> = flow {
        emit(Resource.Loading())
        val movieReviews = movieReviewDao.getAllSavedList(offset)
        if (movieReviews.isNotEmpty()) {
            val item = movieReviewDao.getAllSavedList(page = offset)
            emit(Resource.Success(item.map {
                MovieReviewItemModel(it)
            }))
        }

        val response = movieReviewApiService.getMovieReviews(offset)
        if (response.results?.isNotEmpty() == true) {
            val currentMilliTime = System.currentTimeMillis()
            val results = response.results.map {
                it.toEntityData(pageOffset = offset, currentMilliTime)
            }

            if(offset == 0 && movieReviews.isNotEmpty()){
                movieReviewDao.clearAllData()
            }

            movieReviewDao.insertAllMovieReviews(results)
            val item = movieReviewDao.getAllSavedList(page = offset)
            emit(Resource.Success(item.map {
                MovieReviewItemModel(it)
            }))
        } else {
            emit(Resource.Error())
        }

    }.flowOn(ioDispatcher)
}
