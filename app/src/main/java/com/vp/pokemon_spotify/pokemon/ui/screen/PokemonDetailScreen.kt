package com.vp.pokemon_spotify.pokemon.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vp.pokemon_spotify.pokemon.model.PokemonCardModel
import com.vp.pokemon_spotify.pokemon.ui.widgets.RemoteImage
import com.vp.pokemon_spotify.pokemon.viewmodel.PokemonDetailUiState
import com.vp.pokemon_spotify.pokemon.viewmodel.PokemonDetailViewModel
import java.util.Locale

@Composable
fun PokemonDetailScreen(
    onBackClick: () -> Unit,
    viewModel: PokemonDetailViewModel = viewModel(factory = PokemonDetailViewModel.Factory),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Color(0xFF0D1117),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (val s = state) {
                PokemonDetailUiState.Loading -> {
                    CircularProgressIndicator(
                        color = Color(0xFF22C55E),
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                is PokemonDetailUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(s.message, color = Color(0xFFE53935))
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = viewModel::loadCard,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                        ) {
                            Text("Retry")
                        }
                    }
                }

                is PokemonDetailUiState.Success -> {
                    PokemonDetailContent(
                        card = s.card,
                        onBackClick = onBackClick,
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonDetailContent(
    card: PokemonCardModel,
    onBackClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd,
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF212836))
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            RemoteImage(
                url = card.imageUrl,
                contentDescription = card.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(220.dp)
                    .aspectRatio(0.72f)
                    .clip(RoundedCornerShape(12.dp)),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val titleText = if (card.name.contains(card.cardNumber, ignoreCase = true) || card.cardNumber == "N/A") {
            card.name
        } else {
            "${card.name} - ${card.cardNumber}"
        }

        Text(
            text = titleText,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = card.setName,
            color = Color(0xFF8B949E),
            fontSize = 13.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B26)),
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            tint = Color(0xFF22C55E),
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Market Price",
                            color = Color(0xFF8B949E),
                            fontSize = 11.sp,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = card.marketPrice?.let { String.format(Locale.US, "$%.2f", it) } ?: "-",
                        color = Color(0xFF22C55E),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B26)),
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.LocalOffer,
                            contentDescription = null,
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Lowest Price",
                            color = Color(0xFF8B949E),
                            fontSize = 11.sp,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = card.lowestPrice?.let { String.format(Locale.US, "$%.2f", it) } ?: "-",
                        color = Color(0xFFFBBF24),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Market Activity",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(10.dp))

        DetailRow(label = "Active Listings", value = card.activeListings?.toString() ?: "-")
        DetailRow(label = "Verified Sellers", value = card.verifiedSellers?.toString() ?: "-")
        DetailRow(label = "Primary Printing", value = card.primaryPrinting ?: "-")

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Card Specifications",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(10.dp))

        DetailRow(label = "Card Number", value = card.cardNumber)
        DetailRow(label = "Rarity", value = card.rarity)
        DetailRow(label = "Type", value = card.type)
        DetailRow(label = "Illustrator", value = card.illustrator)

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onBackClick,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            Text(
                text = "Done",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            color = Color(0xFF8B949E),
            fontSize = 13.sp,
        )
        Text(
            text = value,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
