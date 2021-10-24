package com.app.data.model


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Multimedia(
    val height: Int?,
    val src: String?,
    val type: String?,
    val width: Int?
) : Parcelable