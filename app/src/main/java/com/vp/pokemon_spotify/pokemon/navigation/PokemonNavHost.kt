package com.vp.pokemon_spotify.pokemon.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vp.pokemon_spotify.pokemon.ui.screen.PokemonDashboardScreen
import com.vp.pokemon_spotify.pokemon.ui.screen.PokemonDetailScreen

@Composable
fun PokemonNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PokemonDashboardRoute,
    ) {
        composable<PokemonDashboardRoute> {
            PokemonDashboardScreen(
                onCardClick = { cardId ->
                    navController.navigate(PokemonDetailRoute(cardId = cardId))
                },
            )
        }

        composable<PokemonDetailRoute> {
            PokemonDetailScreen(
                onBackClick = { navController.navigateUp() },
            )
        }
    }
}
