package com.vp.pokemon_spotify.spotify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vp.pokemon_spotify.spotify.data.remote.MusicServiceLocator
import com.vp.pokemon_spotify.spotify.data.repository.MusicRepository
import com.vp.pokemon_spotify.spotify.model.AlbumModel
import com.vp.pokemon_spotify.spotify.model.ArtistModel
import com.vp.pokemon_spotify.spotify.util.toUserMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface MusicUiState {
    data object Loading : MusicUiState
    data class Success(
        val artist: ArtistModel,
        val albums: List<AlbumModel>,
    ) : MusicUiState
    data class Error(val message: String) : MusicUiState
}

data class MusicDashboardState(
    val searchQuery: String = "Coldplay",
    val uiState: MusicUiState = MusicUiState.Loading,
    val isBioExpanded: Boolean = false,
)

class MusicViewModel(
    private val repository: MusicRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MusicDashboardState())
    val state: StateFlow<MusicDashboardState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        searchArtist(_state.value.searchQuery)
    }

    fun onQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun toggleBio() {
        _state.update { it.copy(isBioExpanded = !it.isBioExpanded) }
    }

    fun searchArtist(query: String = _state.value.searchQuery) {
        val cleanQuery = query.trim()
        _state.update { it.copy(searchQuery = query, uiState = MusicUiState.Loading, isBioExpanded = false) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (cleanQuery.isBlank()) {
                _state.update { it.copy(uiState = MusicUiState.Error("Nama artis tidak boleh kosong.")) }
                return@launch
            }

            val artistDeferred = async { repository.searchArtist(cleanQuery) }
            val albumsDeferred = async { repository.getArtistAlbums(cleanQuery) }

            val artistResult = artistDeferred.await()
            val albumsResult = albumsDeferred.await()

            if (artistResult.isSuccess) {
                val artist = artistResult.getOrThrow()
                val albums = albumsResult.getOrDefault(emptyList())
                _state.update {
                    it.copy(uiState = MusicUiState.Success(artist = artist, albums = albums))
                }
            } else {
                val error = artistResult.exceptionOrNull()
                _state.update {
                    it.copy(uiState = MusicUiState.Error(error?.toUserMessage() ?: "Gagal memuat artis."))
                }
            }
        }
    }

    fun retry() {
        searchArtist(_state.value.searchQuery)
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { MusicViewModel(MusicServiceLocator.repository) }
        }
    }
}
