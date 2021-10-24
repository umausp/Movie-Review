package com.app.data.utils

import java.net.UnknownHostException

sealed class Resource<out T> {
    data class Loading(val nothing: Nothing? = null) : Resource<Nothing>()

    data class Success<T>(val data: T) : Resource<T>()

    data class Error(
        val throwable: Throwable = RuntimeException("empty data"),
        val retry: () -> Unit = {}
    ) : Resource<Nothing>() {
        init {
            log(throwable)
        }
    }

    fun get(): T? = when (this) {
        is Success -> data
        else -> null
    }

    data class NetworkError(val retry: () -> Unit = {}) : Resource<Nothing>()

    fun onLoading(onResult: () -> Unit): Resource<T> {
        onResult()
        return this
    }

    inline fun onSuccess(onResult: (T) -> Unit) {
        if (this is Success) {
            onResult(data)
        }
        onLoading { /* For Stopping Loader */ }
    }

    inline fun onNetworkError(onResult: (retry: () -> Unit) -> Unit) {
        if (this is NetworkError) {
            onResult(retry)
        }
        onLoading { /* For Stopping Loader */ }
    }

    inline fun onError(onResult: (Error, retry: () -> Unit) -> Unit) {
        if (this is Error) {
            onResult(this, retry)
        }
        onLoading { /* For Stopping Loader */ }
    }

    fun isSuccess() = this is Success
    fun isLoading() = this is Loading
    fun isNetworkError() = this is NetworkError
}

fun Throwable.toResource(retry: () -> Unit): Resource<Nothing>? =
    when (this) {
        is UnknownHostException -> Resource.NetworkError(retry)
        else -> Resource.Error(this, retry)
    }
