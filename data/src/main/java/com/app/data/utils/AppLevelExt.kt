package com.app.data.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


inline fun <T> launchPagingAsync(
    scope: CoroutineScope,
    crossinline execute: suspend () -> Flow<T>,
    crossinline onSuccess: (Flow<T>) -> Unit,
    crossinline onError: (Throwable) -> Unit
) {
    scope.launch {
        try {
            val result = execute()
            onSuccess(result)
        } catch (ex: Exception) {
            onError(ex)
        }
    }
}

fun launchOnLifecycleScope(scope: LifecycleOwner, execute: suspend () -> Unit) {
    scope.lifecycleScope.launchWhenStarted {
        execute()
    }
}
