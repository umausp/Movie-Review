package com.app.moviereview.utils

import android.net.Uri
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.Priority
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder

fun SimpleDraweeView.bindImageUrlWithImage(imgUrl: String?) {
    val imageRequest = ImageRequestBuilder
        .newBuilderWithSource(Uri.parse(imgUrl))
        .setRequestPriority(Priority.HIGH)
        .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
        .build()

    setImageRequest(imageRequest)
}