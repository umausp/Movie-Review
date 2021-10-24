package com.app.data.model


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class MovieReviewModel(
    val copyright: String?,
    val has_more: Boolean?,
    val num_results: Int?,
    val results: List<Result>?,
    val status: String?
) : Parcelable