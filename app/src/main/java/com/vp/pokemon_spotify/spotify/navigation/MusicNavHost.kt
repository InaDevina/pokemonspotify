package com.vp.pokemon_spotify.spotify.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vp.pokemon_spotify.spotify.ui.screen.AlbumDetailScreen
import com.vp.pokemon_spotify.spotify.ui.screen.MusicArtistScreen

@Composable
fun MusicNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MusicArtistRoute,
    ) {
        composable<MusicArtistRoute> {
            MusicArtistScreen(
                onAlbumClick = { albumId, artistName ->
                    navController.navigate(AlbumDetailRoute(albumId = albumId, artistName = artistName))
                },
            )
        }

        composable<AlbumDetailRoute> {
            AlbumDetailScreen(
                onBackClick = { navController.navigateUp() },
            )
        }
    }
}
