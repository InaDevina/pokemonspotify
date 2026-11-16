package com.vp.pokemon_spotify

import com.vp.pokemon_spotify.pokemon.data.dto.PokemonCardResponseDto
import com.vp.pokemon_spotify.pokemon.data.dto.PokemonSearchResponseDto
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    @Test
    fun testParsePokemonSearch() {
        val jsonStr = """
        {
          "data": [
            {
              "id": "696f66d967063f37df821e7c",
              "tcgPlayerId": "96392",
              "setId": 1451,
              "setName": "XY Promos",
              "name": "Charizard EX - XY29",
              "cardNumber": "XY29",
              "totalSetNumber": null,
              "rarity": "Promo",
              "cardType": "Pokemon",
              "pokedexNumbers": [6],
              "hp": "180",
              "artist": "Eske Yoshinob",
              "pokemonType": "Fire",
              "energyType": ["Fire"],
              "prices": {
                "market": 21.78,
                "low": 2.84,
                "sellers": 96,
                "listings": 118,
                "primaryPrinting": "Holofoil"
              },
              "imageCdnUrl": "https://tcgplayer-cdn.tcgplayer.com/product/96392_in_800x800.jpg",
              "imageCdnUrl400": "https://tcgplayer-cdn.tcgplayer.com/product/96392_in_400x400.jpg"
            },
            {
              "id": "item2",
              "name": "Charizard EX",
              "prices": {
                "market": null,
                "low": 600,
                "sellers": 2
              }
            }
          ]
        }
        """.trimIndent()

        val parsed = json.decodeFromString<PokemonSearchResponseDto>(jsonStr)
        assertEquals(2, parsed.data.size)
        assertEquals("Charizard EX - XY29", parsed.data[0].name)
        assertEquals(21.78, parsed.data[0].prices?.market!!, 0.001)
        assertEquals(2.84, parsed.data[0].prices?.low!!, 0.001)
        assertEquals("Eske Yoshinob", parsed.data[0].artist)
        assertEquals("Fire", parsed.data[0].pokemonType)
        assertNull(parsed.data[1].prices?.market)
        assertEquals(600.0, parsed.data[1].prices?.low!!, 0.001)
    }

    @Test
    fun testParsePokemonDetail() {
        val jsonStr = """
        {
          "data": {
            "id": "696f66d967063f37df821e7c",
            "tcgPlayerId": "96392",
            "name": "Charizard EX - XY29",
            "prices": {
              "market": 21.78,
              "low": 2.84
            }
          }
        }
        """.trimIndent()

        val parsed = json.decodeFromString<PokemonCardResponseDto>(jsonStr)
        assertNotNull(parsed.data)
        assertEquals("96392", parsed.data?.tcgPlayerId)
    }
}
