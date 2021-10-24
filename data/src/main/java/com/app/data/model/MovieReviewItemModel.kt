package com.app.data.model

import com.app.data.storage.MovieReviewEntity

class MovieReviewItemModel(private val movieReviewEntity: MovieReviewEntity) {
    val title: String = movieReviewEntity.title
}
