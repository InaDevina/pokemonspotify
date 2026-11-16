package com.vp.pokemon_spotify.spotify.navigation

import kotlinx.serialization.Serializable

@Serializable
data object MusicArtistRoute

@Serializable
data class AlbumDetailRoute(val albumId: String, val artistName: String)
