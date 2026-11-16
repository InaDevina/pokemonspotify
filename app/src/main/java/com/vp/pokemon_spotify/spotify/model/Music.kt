package com.vp.pokemon_spotify.spotify.model

import java.util.Locale

data class ArtistModel(
    val id: String,
    val name: String,
    val genre: String,
    val country: String,
    val biography: String,
    val thumbUrl: String?,
    val bannerUrl: String?,
)

data class AlbumModel(
    val id: String,
    val artistId: String,
    val name: String,
    val artistName: String,
    val year: String,
    val genre: String,
    val thumbUrl: String?,
    val description: String,
)

data class TrackModel(
    val id: String,
    val albumId: String,
    val title: String,
    val trackNumber: String,
    val genre: String,
    val durationFormatted: String,
) {
    companion object {
        fun formatDuration(durationMsString: String?): String {
            val totalSeconds = (durationMsString?.toLongOrNull() ?: 0L) / 1000
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            return String.format(Locale.US, "%02d:%02d", minutes, seconds)
        }

        fun formatTrackNumber(numberStr: String?): String {
            val num = numberStr?.toIntOrNull() ?: 1
            return String.format(Locale.US, "%02d", num)
        }
    }
}

data class AlbumDetailModel(
    val album: AlbumModel,
    val tracks: List<TrackModel>,
)
