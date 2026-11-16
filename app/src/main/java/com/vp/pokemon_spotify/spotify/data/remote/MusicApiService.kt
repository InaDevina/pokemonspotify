package com.vp.pokemon_spotify.spotify.data.remote

import com.vp.pokemon_spotify.spotify.data.dto.AlbumDetailResponseDto
import com.vp.pokemon_spotify.spotify.data.dto.ArtistAlbumsResponseDto
import com.vp.pokemon_spotify.spotify.data.dto.ArtistSearchResponseDto
import com.vp.pokemon_spotify.spotify.data.dto.TrackListResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicApiService {

    @GET("search.php")
    suspend fun searchArtist(
        @Query("s") artistName: String,
    ): ArtistSearchResponseDto

    @GET("searchalbum.php")
    suspend fun searchAlbums(
        @Query("s") artistName: String,
    ): ArtistAlbumsResponseDto

    @GET("album.php")
    suspend fun getAlbumDetail(
        @Query("m") albumId: String,
    ): AlbumDetailResponseDto

    @GET("track.php")
    suspend fun getAlbumTracks(
        @Query("m") albumId: String,
    ): TrackListResponseDto
}
