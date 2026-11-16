package com.vp.pokemon_spotify.spotify.data.remote

import com.vp.pokemon_spotify.BuildConfig
import com.vp.pokemon_spotify.spotify.data.repository.MusicRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object MusicServiceLocator {

    fun getTheAudioDbBaseUrl(): String {
        val raw = BuildConfig.THEAUDIODB_BASE_URL.trim()
        val keyProp = BuildConfig.THEAUDIODB_API_KEY.trim()

        return when {
            raw.contains("/api/v1/json/") -> {
                val idx = raw.indexOf("/api/v1/json/") + "/api/v1/json/".length
                val nextSlash = raw.indexOf('/', idx)
                val key = if (nextSlash != -1) raw.substring(idx, nextSlash) else "523532"
                val effectiveKey = if (key == "123") "523532" else key
                "https://www.theaudiodb.com/api/v1/json/$effectiveKey/"
            }
            keyProp.isNotBlank() -> {
                val effectiveKey = if (keyProp == "123") "523532" else keyProp
                "https://www.theaudiodb.com/api/v1/json/$effectiveKey/"
            }
            raw.isNotBlank() -> {
                if (raw.endsWith("/")) raw else "$raw/"
            }
            else -> "https://www.theaudiodb.com/api/v1/json/523532/"
        }
    }

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC
                else HttpLoggingInterceptor.Level.NONE
            })
            .build()
    }

    val api: MusicApiService by lazy {
        Retrofit.Builder()
            .baseUrl(getTheAudioDbBaseUrl())
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(MusicApiService::class.java)
    }

    val repository: MusicRepository by lazy { MusicRepository(api) }
}
