package com.vp.pokemon_spotify.pokemon.data.repository

import com.vp.pokemon_spotify.pokemon.data.remote.PokemonApiService
import com.vp.pokemon_spotify.pokemon.data.remote.PokemonServiceLocator
import com.vp.pokemon_spotify.pokemon.model.PokemonCardModel
import com.vp.pokemon_spotify.pokemon.util.safeCall
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

class PokemonRepository(private val api: PokemonApiService) {
    private val cardCache = mutableMapOf<String, PokemonCardModel>()
    suspend fun searchCards(query: String): Result<List<PokemonCardModel>> = guarded {
        val response = api.searchCards(search = query, limit = 20)
        val models = response.data.map { it.toDomain() }
        models.forEach { cardCache[it.id] = it }
        models
    }
    suspend fun getCardDetail(cardId: String): Result<PokemonCardModel> {
        cardCache[cardId]?.let { return Result.success(it) }

        return guarded {
            val response = api.getCardById(tcgPlayerId = cardId)
            val card = response.data?.toDomain()
                ?: throw NoSuchElementException("Kartu tidak ditemukan")
            cardCache[card.id] = card
            card
        }
    }
    private suspend fun <T> guarded(block: suspend () -> T): Result<T> {
        val apiKey = PokemonServiceLocator.getPokemonApiKey().trim()
        if (apiKey.isBlank()) {
            val response = Response.error<Any>(
                401,
                "HTTP 401: API key belum diisi. Masukkan API key di local.properties atau PokemonServiceLocator."
                    .toResponseBody("text/plain".toMediaType())
            )
            return Result.failure(HttpException(response))
        }
        return safeCall(block)
    }
}
