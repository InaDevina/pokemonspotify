package com.vp.pokemon_spotify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vp.pokemon_spotify.pokemon.navigation.PokemonNavHost
import com.vp.pokemon_spotify.spotify.navigation.MusicNavHost
import com.vp.pokemon_spotify.ui.theme.PokemonSpotifyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokemonSpotifyTheme {
                //Comment atau uncomment salah satu NavHost di bawah untuk menjalankan soal:
                //soal 1 (pokemon)
                PokemonNavHost()

                //soal 2 (coldplay)
                //MusicNavHost()
            }
        }
    }
}
