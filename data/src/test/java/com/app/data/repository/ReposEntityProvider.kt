package com.app.data.repository

import com.app.data.storage.MovieReviewEntity

object ReposEntityProvider {

    fun createListRepoEntity() : List<MovieReviewEntity> {
        return mutableListOf(
            MovieReviewEntity(

            ),
            MovieReviewEntity(

            )
        )
    }
}