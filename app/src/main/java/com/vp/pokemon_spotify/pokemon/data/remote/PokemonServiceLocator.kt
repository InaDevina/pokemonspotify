package com.vp.pokemon_spotify.pokemon.data.remote

import com.vp.pokemon_spotify.BuildConfig
import com.vp.pokemon_spotify.pokemon.data.repository.PokemonRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object PokemonServiceLocator {
    fun getPokemonApiKey(): String {
        val raw = BuildConfig.POKEMON_API_KEY.trim()
        val prefix = "pokeprice_free_"
        return if (raw.startsWith(prefix, ignoreCase = true)) {
            prefix + raw.substring(prefix.length).replace('O', '0').replace('o', '0')
        } else {
            raw.replace('O', '0').replace('o', '0')
        }
    }

    private const val BASE_URL = "https://www.pokemonpricetracker.com/"

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(PokemonApiKeyInterceptor { getPokemonApiKey() })
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC
                else HttpLoggingInterceptor.Level.NONE
            })
            .build()
    }

    val api: PokemonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PokemonApiService::class.java)
    }

    val repository: PokemonRepository by lazy { PokemonRepository(api) }
}
