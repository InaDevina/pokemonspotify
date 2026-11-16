package com.vp.pokemon_spotify.pokemon.data.repository

import com.vp.pokemon_spotify.pokemon.data.dto.PokemonCardDto
import com.vp.pokemon_spotify.pokemon.model.PokemonCardModel

fun PokemonCardDto.toDomain(): PokemonCardModel {
    val cleanCardNumber = cardNumber?.trim().orEmpty().ifEmpty { "N/A" }
    val displayArtist = artist?.ifBlank { null } ?: illustrator?.ifBlank { null } ?: "Unknown"
    val displayType = pokemonType?.ifBlank { null } ?: energyType.firstOrNull() ?: cardType ?: "Colorless"
    val chosenImage = imageCdnUrl400 ?: imageCdnUrl ?: imageCdnUrl800 ?: imageCdnUrl200 ?: imageUrl

    return PokemonCardModel(
        id = tcgPlayerId ?: id ?: "${name}_$cleanCardNumber",
        name = name,
        setName = setName ?: "Unknown Set",
        cardNumber = cleanCardNumber,
        rarity = rarity ?: "Common",
        type = displayType,
        illustrator = displayArtist,
        imageUrl = chosenImage,
        marketPrice = prices?.market,
        lowestPrice = prices?.low,
        activeListings = prices?.listings,
        verifiedSellers = prices?.sellers,
        primaryPrinting = prices?.primaryPrinting ?: "Normal",
    )
}
