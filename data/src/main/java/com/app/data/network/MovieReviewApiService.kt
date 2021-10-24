package com.app.data.network

import com.app.data.model.MovieReviewModel
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieReviewApiService {
    @GET("reviews/all.json")
    suspend fun getMovieReviews(@Query("offset") offset: Int): MovieReviewModel
}
