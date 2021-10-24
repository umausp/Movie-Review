package com.app.data.utils

import kotlinx.coroutines.Job
import java.io.IOException
import java.net.UnknownHostException

sealed class Resource<out T> {
    data class Loading(val job: Job? = null) : Resource<Nothing>()

    data class Success<T>(val data: T) : Resource<T>()

    data class Error(
        val throwable: Throwable = RuntimeException("empty error"),
        val retry: () -> Unit = {}
    ) : Resource<Nothing>() {
        init {
            log(throwable)
        }
    }

    data class NetworkError(val retry: () -> Unit = {}) : Resource<Nothing>()

    fun get(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun onLoading(onResult: (job: Job?) -> Unit): Resource<T> {
        if (this is Loading) {
            onResult(job)
        }
        return this
    }

    inline fun onSuccess(onResult: (T) -> Unit) {
        if (this is Success) {
            onResult(data)
        }
    }

    inline fun onNetworkError(onResult: (retry: () -> Unit) -> Unit) {
        if (this is NetworkError) {
            onResult(retry)
        }
    }

    inline fun onError(onResult: (Error, retry: () -> Unit) -> Unit) {
        if (this is Error) {
            onResult(this, retry)
        }
    }

    fun isSuccess() = this is Success
    fun isLoading() = this is Loading
    fun isResult() = !isLoading()
    fun isNetworkError() = this is NetworkError
}

fun Throwable.toResource(retry: () -> Unit): Resource<Nothing>? =
    when (this) {
        is UnknownHostException -> Resource.NetworkError(retry)
        else -> Resource.Error(this, retry)
    }
