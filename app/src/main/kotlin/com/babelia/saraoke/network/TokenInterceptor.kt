package com.babelia.saraoke.network

import com.babelia.saraoke.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * OkHttp [Interceptor] used to add the authorization header with the API token in all the Retrofit requests.
 */
class TokenInterceptor : Interceptor {

    companion object {
        private const val REQUEST_HEADER_AUTHORIZATION = "Authorization"
        private const val REQUEST_HEADER_BEARER_PREFIX = "Bearer"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader(REQUEST_HEADER_AUTHORIZATION, "$REQUEST_HEADER_BEARER_PREFIX ${BuildConfig.GENIUS_API_TOKEN}")
            .build()
        return chain.proceed(request)
    }
}