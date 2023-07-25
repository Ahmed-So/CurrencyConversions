package com.example.currencyconversions.data.remote

import com.example.currencyconversions.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val url: HttpUrl = chain.request().url().newBuilder()
            .addQueryParameter("access_key", BuildConfig.FIXER_API_KEY)
            .build()
        return chain.proceed(
            chain.request().newBuilder()
                .url(url)
                .build()
        )
    }
}