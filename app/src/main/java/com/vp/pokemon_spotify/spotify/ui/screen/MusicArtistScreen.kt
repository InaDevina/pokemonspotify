package com.vp.pokemon_spotify.spotify.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vp.pokemon_spotify.spotify.model.AlbumModel
import com.vp.pokemon_spotify.spotify.model.ArtistModel
import com.vp.pokemon_spotify.spotify.ui.widgets.AlbumCard
import com.vp.pokemon_spotify.spotify.ui.widgets.RemoteImage
import com.vp.pokemon_spotify.spotify.ui.widgets.SearchInputField
import com.vp.pokemon_spotify.spotify.ui.widgets.SpotifyWaveIcon
import com.vp.pokemon_spotify.spotify.viewmodel.MusicDashboardState
import com.vp.pokemon_spotify.spotify.viewmodel.MusicUiState
import com.vp.pokemon_spotify.spotify.viewmodel.MusicViewModel

@Composable
fun MusicArtistScreen(
    onAlbumClick: (String, String) -> Unit,
    viewModel: MusicViewModel = viewModel(factory = MusicViewModel.Factory),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MusicArtistContent(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onSearch = viewModel::searchArtist,
        onToggleBio = viewModel::toggleBio,
        onRetry = viewModel::retry,
        onAlbumClick = onAlbumClick,
    )
}

@Composable
fun MusicArtistContent(
    state: MusicDashboardState,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onToggleBio: () -> Unit,
    onRetry: () -> Unit,
    onAlbumClick: (String, String) -> Unit,
) {
    Scaffold(
        containerColor = Color(0xFF121212),
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
            ) {
                SpotifyWaveIcon(modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Music Artist",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SearchInputField(
                query = state.searchQuery,
                onQueryChange = onQueryChange,
                onSearch = { onSearch(state.searchQuery) },
                placeholder = "Coldplay",
                backgroundColor = Color(0xFF242424),
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (val uiState = state.uiState) {
                MusicUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = Color(0xFF1DB954))
                    }
                }

                is MusicUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(24.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ErrorOutline,
                                contentDescription = "Error",
                                tint = Color(0xFFE53935),
                                modifier = Modifier.size(48.dp),
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Terjadi Kendala",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = uiState.message,
                                color = Color(0xFFA7A7A7),
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = onRetry,
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF1DB954),
                                ),
                                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 8.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = "Coba Lagi",
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp),
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Coba Lagi",
                                    color = Color.Black,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                }

                is MusicUiState.Success -> {
                    ArtistSuccessBody(
                        artist = uiState.artist,
                        albums = uiState.albums,
                        isBioExpanded = state.isBioExpanded,
                        onToggleBio = onToggleBio,
                        onAlbumClick = onAlbumClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun ArtistSuccessBody(
    artist: ArtistModel,
    albums: List<AlbumModel>,
    isBioExpanded: Boolean,
    onToggleBio: () -> Unit,
    onAlbumClick: (String, String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 24.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(12.dp)),
            ) {
                RemoteImage(
                    url = artist.bannerUrl,
                    contentDescription = artist.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0xCC000000)),
                                startY = 100f,
                            ),
                        ),
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RemoteImage(
                        url = artist.thumbUrl,
                        contentDescription = artist.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, Color.White, CircleShape),
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = artist.name,
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = artist.genre,
                                color = Color(0xFF1DB954),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                text = " • ${artist.country}",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 11.sp,
                            )
                        }
                    }
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tentang Artis",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = artist.biography,
                    color = Color(0xFFA7A7A7),
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    maxLines = if (isBioExpanded) Int.MAX_VALUE else 4,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isBioExpanded) "Tutup ringkasan" else "Baca selengkapnya",
                    color = Color(0xFF1DB954),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onToggleBio() },
                )
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "Diskografi Album",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Koleksi rilisan resmi",
                        color = Color(0xFFA7A7A7),
                        fontSize = 11.sp,
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF133621))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "${albums.size} Album",
                        color = Color(0xFF1DB954),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

        items(albums, key = { it.id }) { album ->
            AlbumCard(
                album = album,
                onClick = { onAlbumClick(album.id, artist.name) },
            )
        }
    }
}
