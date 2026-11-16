package com.vp.pokemon_spotify.spotify.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArtistSearchResponseDto(
    val artists: List<ArtistDto>? = null,
)

@Serializable
data class ArtistDto(
    val idArtist: String? = null,
    val strArtist: String? = null,
    val strGenre: String? = null,
    val strCountry: String? = null,
    val strBiography: String? = null,
    val strBiographyEN: String? = null,
    val strArtistThumb: String? = null,
    val strArtistBanner: String? = null,
    val strArtistFanart: String? = null,
    val strArtistWideThumb: String? = null,
)

@Serializable
data class ArtistAlbumsResponseDto(
    val album: List<AlbumDto>? = null,
)

@Serializable
data class AlbumDetailResponseDto(
    val album: List<AlbumDto>? = null,
)

@Serializable
data class AlbumDto(
    val idAlbum: String? = null,
    val idArtist: String? = null,
    val strAlbum: String? = null,
    val strArtist: String? = null,
    val intYearReleased: String? = null,
    val strGenre: String? = null,
    val strAlbumThumb: String? = null,
    val strDescription: String? = null,
    val strDescriptionEN: String? = null,
)

@Serializable
data class TrackListResponseDto(
    val track: List<TrackDto>? = null,
)

@Serializable
data class TrackDto(
    val idTrack: String? = null,
    val idAlbum: String? = null,
    val strTrack: String? = null,
    val strArtist: String? = null,
    val strGenre: String? = null,
    val intTrackNumber: String? = null,
    val intDuration: String? = null,
)
