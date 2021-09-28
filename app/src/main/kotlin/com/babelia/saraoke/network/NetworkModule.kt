package com.babelia.saraoke.network

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
object NetworkModule {

    const val GENIUS_URL = "https://genius.com"
    private const val HTTP_CLIENT_TIMEOUT_SECONDS = 60L
    private const val CACHE_SIZE = 300L * 1024 * 1024 // 300 MB

    fun create() = DI.Module("NetworkModule") {

        bind<OkHttpClient>() with singleton {
            val context: Context = instance()

            OkHttpClient.Builder()
                .connectTimeout(HTTP_CLIENT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(HTTP_CLIENT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(HTTP_CLIENT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .cache(Cache(context.cacheDir, CACHE_SIZE))
                .addInterceptor(TokenInterceptor())
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
        }

        bind<Retrofit>() with singleton {
            val endpoint = "https://api.genius.com/"

            Retrofit.Builder()
                .baseUrl(endpoint.toHttpUrl())
                .client(instance())
                .addConverterFactory(MoshiConverterFactory.create(instance()))
                .build()
        }

        bind<Moshi>() with singleton {
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        }

    }
}