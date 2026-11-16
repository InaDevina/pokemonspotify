package com.vp.pokemon_spotify.pokemon.data.remote

import com.vp.pokemon_spotify.pokemon.data.dto.PokemonCardResponseDto
import com.vp.pokemon_spotify.pokemon.data.dto.PokemonSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApiService {
    @GET("api/v2/cards")
    suspend fun searchCards(
        @Query("search") search: String,
        @Query("limit") limit: Int = 20,
    ): PokemonSearchResponseDto

    @GET("api/v2/cards")
    suspend fun getCardById(
        @Query("tcgPlayerId") tcgPlayerId: String,
    ): PokemonCardResponseDto
}
