package com.vp.pokemon_spotify.spotify.data.repository

import com.vp.pokemon_spotify.spotify.data.dto.AlbumDto
import com.vp.pokemon_spotify.spotify.data.dto.ArtistDto
import com.vp.pokemon_spotify.spotify.data.dto.TrackDto
import com.vp.pokemon_spotify.spotify.model.AlbumModel
import com.vp.pokemon_spotify.spotify.model.ArtistModel
import com.vp.pokemon_spotify.spotify.model.TrackModel

fun ArtistDto.toDomain(): ArtistModel {
    val bio = strBiographyEN?.takeIf { it.isNotBlank() }
        ?: strBiography?.takeIf { it.isNotBlank() }
        ?: "Biografi tidak tersedia."
    val banner = strArtistBanner
        ?: strArtistFanart
        ?: strArtistWideThumb
        ?: strArtistThumb

    return ArtistModel(
        id = idArtist.orEmpty(),
        name = strArtist.orEmpty(),
        genre = strGenre.orEmpty().ifEmpty { "Various" },
        country = strCountry.orEmpty().ifEmpty { "Unknown" },
        biography = bio,
        thumbUrl = strArtistThumb,
        bannerUrl = banner,
    )
}

fun AlbumDto.toDomain(): AlbumModel {
    val desc = strDescriptionEN?.takeIf { it.isNotBlank() }
        ?: strDescription?.takeIf { it.isNotBlank() }
        ?: "Deskripsi album tidak tersedia."

    return AlbumModel(
        id = idAlbum.orEmpty(),
        artistId = idArtist.orEmpty(),
        name = strAlbum.orEmpty(),
        artistName = strArtist.orEmpty(),
        year = intYearReleased.orEmpty().ifEmpty { "-" },
        genre = strGenre.orEmpty().ifEmpty { "Pop/Rock" },
        thumbUrl = strAlbumThumb,
        description = desc,
    )
}

fun TrackDto.toDomain(): TrackModel {
    return TrackModel(
        id = idTrack.orEmpty(),
        albumId = idAlbum.orEmpty(),
        title = strTrack.orEmpty(),
        trackNumber = TrackModel.formatTrackNumber(intTrackNumber),
        genre = strGenre.orEmpty().ifEmpty { "Music" },
        durationFormatted = TrackModel.formatDuration(intDuration),
    )
}
