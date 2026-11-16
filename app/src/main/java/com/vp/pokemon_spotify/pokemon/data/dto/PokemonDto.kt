package com.vp.pokemon_spotify.pokemon.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PokemonSearchResponseDto(
    val data: List<PokemonCardDto> = emptyList(),
)

@Serializable
data class PokemonCardResponseDto(
    val data: PokemonCardDto? = null,
)

@Serializable
data class PokemonCardDto(
    val id: String? = null,
    val tcgPlayerId: String? = null,
    val name: String = "",
    val setName: String? = null,
    val cardNumber: String? = null,
    val rarity: String? = null,
    val cardType: String? = null,
    val hp: String? = null,
    val pokemonType: String? = null,
    val energyType: List<String> = emptyList(),
    val artist: String? = null,
    val illustrator: String? = null,
    val imageCdnUrl: String? = null,
    val imageCdnUrl200: String? = null,
    val imageCdnUrl400: String? = null,
    val imageCdnUrl800: String? = null,
    val imageUrl: String? = null,
    val prices: PokemonPricesDto? = null,
)

@Serializable
data class PokemonPricesDto(
    val market: Double? = null,
    val low: Double? = null,
    val sellers: Int? = null,
    val listings: Int? = null,
    val primaryPrinting: String? = null,
    val lastUpdated: String? = null,
)
