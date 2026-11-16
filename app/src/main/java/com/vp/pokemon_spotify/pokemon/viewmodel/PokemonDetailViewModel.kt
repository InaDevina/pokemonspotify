package com.vp.pokemon_spotify.pokemon.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.vp.pokemon_spotify.pokemon.data.remote.PokemonServiceLocator
import com.vp.pokemon_spotify.pokemon.data.repository.PokemonRepository
import com.vp.pokemon_spotify.pokemon.model.PokemonCardModel
import com.vp.pokemon_spotify.pokemon.navigation.PokemonDetailRoute
import com.vp.pokemon_spotify.pokemon.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface PokemonDetailUiState {
    data object Loading : PokemonDetailUiState
    data class Success(val card: PokemonCardModel) : PokemonDetailUiState
    data class Error(val message: String) : PokemonDetailUiState
}

class PokemonDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: PokemonRepository,
) : ViewModel() {

    private val cardId: String = savedStateHandle.toRoute<PokemonDetailRoute>().cardId
    private val _uiState = MutableStateFlow<PokemonDetailUiState>(PokemonDetailUiState.Loading)
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()
    init {
        loadCard()
    }

    fun loadCard() {
        _uiState.value = PokemonDetailUiState.Loading
        viewModelScope.launch {
            repository.getCardDetail(cardId)
                .onSuccess { _uiState.value = PokemonDetailUiState.Success(it) }
                .onFailure { _uiState.value = PokemonDetailUiState.Error(it.toUserMessage()) }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                PokemonDetailViewModel(createSavedStateHandle(), PokemonServiceLocator.repository)
            }
        }
    }
}
