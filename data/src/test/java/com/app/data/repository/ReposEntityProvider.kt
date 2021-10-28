package com.app.data.repository

import com.app.data.model.MovieReviewModel
import com.app.data.model.Result
import com.app.data.storage.MovieReviewEntity

object ReposEntityProvider {
    val createListResult = mutableListOf(
        Result(
            "", 1, "", "test", "test", null, "", null, "",
            "", ""
        ),
        Result(
            "", 1, "", "test1", "test1", null, "", null, "",
            "", ""
        )
    )

    val INFO_FIRST_PAGE = MovieReviewEntity(
        title = "Hulk"
    )

    val API_RESPONSE_FAKE = MovieReviewModel(
        copyright = "", page = 1, has_more = true, status = "ok", num_results = 20, results = createListResult
    )


    fun createListRepoEntity(): MutableList<MovieReviewEntity> {
        return mutableListOf(
            MovieReviewEntity(
                title = "Hulk"
            ),
            MovieReviewEntity(
                title = "Clint"
            )
        )
    }

    fun createListRepoEntityFromResult(result: MutableList<Result>): MutableList<MovieReviewEntity> {
        return mutableListOf(
            MovieReviewEntity(
                title = result[0].display_title.orEmpty()
            )
        )
    }


}