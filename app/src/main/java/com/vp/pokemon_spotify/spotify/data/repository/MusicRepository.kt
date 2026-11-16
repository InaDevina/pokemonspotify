package com.vp.pokemon_spotify.spotify.data.repository

import com.vp.pokemon_spotify.spotify.data.remote.MusicApiService
import com.vp.pokemon_spotify.spotify.model.AlbumDetailModel
import com.vp.pokemon_spotify.spotify.model.AlbumModel
import com.vp.pokemon_spotify.spotify.model.ArtistModel
import com.vp.pokemon_spotify.spotify.util.safeCall

class MusicRepository(private val api: MusicApiService) {

    private val albumCache = mutableMapOf<String, AlbumModel>()

    suspend fun searchArtist(artistName: String): Result<ArtistModel> = safeCall {
        val response = api.searchArtist(artistName)
        val artist = response.artists?.firstOrNull()?.toDomain()
            ?: throw NoSuchElementException("Artis '$artistName' tidak ditemukan.")
        artist
    }

    suspend fun getArtistAlbums(artistName: String): Result<List<AlbumModel>> = safeCall {
        val response = api.searchAlbums(artistName)
        val list = response.album?.map { it.toDomain() } ?: emptyList()
        list.forEach { albumCache[it.id] = it }
        list
    }

    suspend fun getAlbumDetail(albumId: String): Result<AlbumDetailModel> = safeCall {
        val cached = albumCache[albumId]
        val album = try {
            val albumResponse = api.getAlbumDetail(albumId)
            albumResponse.album?.firstOrNull()?.toDomain() ?: cached
        } catch (_: Exception) {
            cached
        } ?: throw NoSuchElementException("Detail album tidak ditemukan.")

        val tracks = try {
            val trackResponse = api.getAlbumTracks(albumId)
            trackResponse.track?.map { it.toDomain() } ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }

        AlbumDetailModel(album = album, tracks = tracks)
    }
}
