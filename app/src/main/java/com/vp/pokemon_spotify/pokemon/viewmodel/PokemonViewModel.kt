package com.vp.pokemon_spotify.pokemon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vp.pokemon_spotify.pokemon.data.remote.PokemonServiceLocator
import com.vp.pokemon_spotify.pokemon.data.repository.PokemonRepository
import com.vp.pokemon_spotify.pokemon.model.PokemonCardModel
import com.vp.pokemon_spotify.pokemon.util.toUserMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface PokemonUiState {
    data object Loading : PokemonUiState
    data class Success(val cards: List<PokemonCardModel>) : PokemonUiState
    data class Error(val message: String) : PokemonUiState
}

data class PokemonDashboardState(
    val searchQuery: String = "Charizard",
    val uiState: PokemonUiState = PokemonUiState.Loading,
)

class PokemonViewModel(
    private val repository: PokemonRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(PokemonDashboardState())
    val state: StateFlow<PokemonDashboardState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        searchCards(_state.value.searchQuery)
    }

    fun onQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun searchCards(query: String = _state.value.searchQuery) {
        val cleanQuery = query.trim()
        _state.update { it.copy(searchQuery = query, uiState = PokemonUiState.Loading) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (cleanQuery.isBlank()) {
                _state.update { it.copy(uiState = PokemonUiState.Success(emptyList())) }
                return@launch
            }
            repository.searchCards(cleanQuery)
                .onSuccess { cards ->
                    _state.update { it.copy(uiState = PokemonUiState.Success(cards)) }
                }
                .onFailure { error ->
                    _state.update { it.copy(uiState = PokemonUiState.Error(error.toUserMessage())) }
                }
        }
    }

    fun retry() {
        searchCards(_state.value.searchQuery)
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { PokemonViewModel(PokemonServiceLocator.repository) }
        }
    }
}
