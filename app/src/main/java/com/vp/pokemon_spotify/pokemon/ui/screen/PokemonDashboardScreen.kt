package com.vp.pokemon_spotify.pokemon.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vp.pokemon_spotify.pokemon.ui.widgets.LiveApiBadge
import com.vp.pokemon_spotify.pokemon.ui.widgets.PokeBallIcon
import com.vp.pokemon_spotify.pokemon.ui.widgets.PokemonCard
import com.vp.pokemon_spotify.pokemon.ui.widgets.SearchInputField
import com.vp.pokemon_spotify.pokemon.viewmodel.PokemonDashboardState
import com.vp.pokemon_spotify.pokemon.viewmodel.PokemonUiState
import com.vp.pokemon_spotify.pokemon.viewmodel.PokemonViewModel

@Composable
fun PokemonDashboardScreen(
    onCardClick: (String) -> Unit,
    viewModel: PokemonViewModel = viewModel(factory = PokemonViewModel.Factory),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PokemonDashboardContent(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onSearch = viewModel::searchCards,
        onTagClick = { tag ->
            viewModel.onQueryChange(tag)
            viewModel.searchCards(tag)
        },
        onRetry = viewModel::retry,
        onCardClick = onCardClick,
    )
}

@Composable
fun PokemonDashboardContent(
    state: PokemonDashboardState,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onTagClick: (String) -> Unit,
    onRetry: () -> Unit,
    onCardClick: (String) -> Unit,
) {
    val quickChips = listOf("Charizard", "Pikachu", "Mewtwo", "Gengar")

    Scaffold(
        containerColor = Color(0xFF0D1117),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PokeBallIcon(modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "PokéTracker",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "Real-time TCG Card Valuation",
                            color = Color(0xFF8B949E),
                            fontSize = 11.sp,
                        )
                    }
                }
                LiveApiBadge()
            }

            Spacer(modifier = Modifier.height(16.dp))

            SearchInputField(
                query = state.searchQuery,
                onQueryChange = onQueryChange,
                onSearch = { onSearch(state.searchQuery) },
                placeholder = "Charizard",
                backgroundColor = Color(0xFF161B26),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                quickChips.forEach { chipName ->
                    val isSelected = state.searchQuery.equals(chipName, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) Color(0xFF263248) else Color(0xFF161B26))
                            .clickable { onTagClick(chipName) }
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = chipName,
                            color = if (isSelected) Color.White else Color(0xFF8B949E),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (val uiState = state.uiState) {
                PokemonUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = Color(0xFF22C55E))
                    }
                }

                is PokemonUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(24.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF212836)),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.WifiOff,
                                    contentDescription = "Connection Failed",
                                    tint = Color(0xFFE53935),
                                    modifier = Modifier.size(28.dp),
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Connection Failed",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = uiState.message,
                                color = Color(0xFF8B949E),
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = onRetry,
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFE53935),
                                ),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = "Try Again",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp),
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Try Again",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                        }
                    }
                }

                is PokemonUiState.Success -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Market Results (${uiState.cards.size})",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "USD • Near Mint",
                            color = Color(0xFF8B949E),
                            fontSize = 11.sp,
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (uiState.cards.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "Tidak ada kartu ditemukan.",
                                color = Color(0xFF8B949E),
                                fontSize = 14.sp,
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 24.dp),
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            items(uiState.cards, key = { it.id }) { card ->
                                PokemonCard(
                                    card = card,
                                    onClick = { onCardClick(card.id) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
