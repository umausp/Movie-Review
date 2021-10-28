package com.app.moviereview.utils

import com.app.data.storage.MovieReviewEntity

object ReposEntityProvider {

    fun createListRepoEntity(): List<MovieReviewEntity> {
        return mutableListOf(
            MovieReviewEntity(
                title = "Groot"
            ),
            MovieReviewEntity(
                title = "Clint"
            ),
            MovieReviewEntity(
                title = "Hulk"
            ),
            MovieReviewEntity(
                title = "Avengers"
            )
        )
    }
}
