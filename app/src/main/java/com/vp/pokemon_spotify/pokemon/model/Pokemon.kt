package com.vp.pokemon_spotify.pokemon.model

data class PokemonCardModel(
    val id: String,
    val name: String,
    val setName: String,
    val cardNumber: String,
    val rarity: String,
    val type: String,
    val illustrator: String,
    val imageUrl: String?,
    val marketPrice: Double?,
    val lowestPrice: Double?,
    val activeListings: Int?,
    val verifiedSellers: Int?,
    val primaryPrinting: String?,
)
