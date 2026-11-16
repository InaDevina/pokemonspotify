package com.vp.pokemon_spotify.spotify.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.vp.pokemon_spotify.spotify.data.remote.MusicServiceLocator
import com.vp.pokemon_spotify.spotify.data.repository.MusicRepository
import com.vp.pokemon_spotify.spotify.model.AlbumDetailModel
import com.vp.pokemon_spotify.spotify.navigation.AlbumDetailRoute
import com.vp.pokemon_spotify.spotify.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface AlbumDetailUiState {
    data object Loading : AlbumDetailUiState
    data class Success(val detail: AlbumDetailModel) : AlbumDetailUiState
    data class Error(val message: String) : AlbumDetailUiState
}

data class AlbumDetailState(
    val uiState: AlbumDetailUiState = AlbumDetailUiState.Loading,
    val isDescExpanded: Boolean = false,
)

class AlbumDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: MusicRepository,
) : ViewModel() {

    private val route: AlbumDetailRoute = savedStateHandle.toRoute<AlbumDetailRoute>()
    val albumId: String = route.albumId
    val artistName: String = route.artistName

    private val _state = MutableStateFlow(AlbumDetailState())
    val state: StateFlow<AlbumDetailState> = _state.asStateFlow()

    init {
        loadDetail()
    }

    fun toggleDesc() {
        _state.update { it.copy(isDescExpanded = !it.isDescExpanded) }
    }

    fun loadDetail() {
        _state.update { it.copy(uiState = AlbumDetailUiState.Loading, isDescExpanded = false) }
        viewModelScope.launch {
            repository.getAlbumDetail(albumId)
                .onSuccess { detail ->
                    _state.update { it.copy(uiState = AlbumDetailUiState.Success(detail)) }
                }
                .onFailure { error ->
                    _state.update { it.copy(uiState = AlbumDetailUiState.Error(error.toUserMessage())) }
                }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                AlbumDetailViewModel(createSavedStateHandle(), MusicServiceLocator.repository)
            }
        }
    }
}
