package com.babelia.saraoke.network

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Class in charge of the communication with the Genius API using Retrofit.
 */
@Suppress("UndocumentedPublicFunction")
interface LyricsApi {

    @GET("/search")
    suspend fun getLyricsBySearch(@Query("q") query: String? = null): QuerySearchResult
}
