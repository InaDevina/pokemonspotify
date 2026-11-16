package com.vp.pokemon_spotify.pokemon.data.remote

import okhttp3.Interceptor
import okhttp3.Response


class PokemonApiKeyInterceptor(private val apiKeyProvider: () -> String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val key = apiKeyProvider().trim()
        val builder = original.newBuilder()
        if (key.isNotEmpty()) {
            builder.header("Authorization", "Bearer $key")
        }
        return chain.proceed(builder.build())
    }
}
