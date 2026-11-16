package com.vp.pokemon_spotify.pokemon.navigation

import kotlinx.serialization.Serializable

@Serializable
data object PokemonDashboardRoute

@Serializable
data class PokemonDetailRoute(val cardId: String)
