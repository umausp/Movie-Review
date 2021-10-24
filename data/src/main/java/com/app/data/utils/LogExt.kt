package com.app.data.utils

import timber.log.Timber

@Suppress("NOTHING_TO_INLINE")
inline fun Any.log(e: Throwable) {
    Timber.e(e)
}
