package com.app.data.usecase

sealed class RequestResult<out T> {
    data class Success<T>(val result: T) : RequestResult<T>()
    data class Failure(val throwable: Throwable) : RequestResult<Nothing>()
}
