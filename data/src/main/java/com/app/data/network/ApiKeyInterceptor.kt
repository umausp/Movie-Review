package com.app.data.network

import com.app.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor() : Interceptor {
    companion object {
        private const val API_KEY = "api-key"
    }
    override fun intercept(chain: Interceptor.Chain): Response = chain.request().let {
        val url = it.url().newBuilder()
            .addQueryParameter(API_KEY, BuildConfig.API_KEY)
            .build()

        val newRequest = it.newBuilder()
            .url(url)
            .build()

        chain.proceed(newRequest)
    }
}
