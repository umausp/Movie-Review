package com.app.data.model


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Link(
    val suggested_link_text: String?,
    val type: String?,
    val url: String?
) : Parcelable