package com.app.data.repository

import com.app.data.model.MovieReviewModel
import com.app.data.network.MovieReviewApiService
import com.app.data.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieReviewRepository @Inject constructor(private val movieReviewApiService: MovieReviewApiService) {
    suspend fun getMovieReviews(offset: Int): Flow<Resource<MovieReviewModel>> = flow {
        emit(Resource.Success(movieReviewApiService.getMovieReviews(offset)))
    }
}
