package com.app.moviereview.utils

import com.app.data.storage.MovieReviewEntity
import javax.inject.Inject

class AppUtils @Inject constructor() {

    fun canStartFiltering(query: String?): Boolean {
        return query?.length?.compareTo(2) == 1
    }

    fun sortMovieEntity(listItems: List<MovieReviewEntity>): List<MovieReviewEntity> {
        return listItems.sortedBy { it.title }
    }
}
