package com.app.moviereview.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.databinding.BindingAdapter
import timber.log.Timber

@BindingAdapter("android:onClick")
fun setOnClickNavigate(view: View, @IdRes id: Int) {
    view.setOnClickListener {
        Timber.d("button Click", it.id.toString())
    }
}
