package com.vp.pokemon_spotify.spotify.ui.screen

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vp.pokemon_spotify.spotify.model.AlbumDetailModel
import com.vp.pokemon_spotify.spotify.ui.widgets.RemoteImage
import com.vp.pokemon_spotify.spotify.ui.widgets.TrackItem
import com.vp.pokemon_spotify.spotify.viewmodel.AlbumDetailState
import com.vp.pokemon_spotify.spotify.viewmodel.AlbumDetailUiState
import com.vp.pokemon_spotify.spotify.viewmodel.AlbumDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    onBackClick: () -> Unit,
    viewModel: AlbumDetailViewModel = viewModel(factory = AlbumDetailViewModel.Factory),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Color(0xFF121212),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detail Album",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121212),
                ),
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (val uiState = state.uiState) {
                AlbumDetailUiState.Loading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF1DB954),
                            modifier = Modifier.size(36.dp),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Memuat detail album & track list...",
                            color = Color(0xFFA7A7A7),
                            fontSize = 13.sp,
                        )
                    }
                }

                is AlbumDetailUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ErrorOutline,
                            contentDescription = "Error",
                            tint = Color(0xFFE53935),
                            modifier = Modifier.size(44.dp),
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Terjadi Kendala",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = uiState.message,
                            color = Color(0xFFA7A7A7),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = viewModel::loadDetail,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
                            shape = RoundedCornerShape(20.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Coba Lagi", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                is AlbumDetailUiState.Success -> {
                    AlbumDetailBody(
                        detail = uiState.detail,
                        artistName = viewModel.artistName,
                        isDescExpanded = state.isDescExpanded,
                        onToggleDesc = viewModel::toggleDesc,
                    )
                }
            }
        }
    }
}

@Composable
private fun AlbumDetailBody(
    detail: AlbumDetailModel,
    artistName: String,
    isDescExpanded: Boolean,
    onToggleDesc: () -> Unit,
) {
    val album = detail.album
    val tracks = detail.tracks

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            RemoteImage(
                url = album.thumbUrl,
                contentDescription = album.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(200.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp)),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = album.name,
            color = Color.White,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = if (album.artistName.isNotBlank()) album.artistName else artistName,
            color = Color(0xFF1DB954),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF242424))
                    .padding(horizontal = 12.dp, vertical = 5.dp),
            ) {
                Text(
                    text = "Tahun ${album.year}",
                    color = Color.White,
                    fontSize = 11.sp,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF242424))
                    .padding(horizontal = 12.dp, vertical = 5.dp),
            ) {
                Text(
                    text = album.genre,
                    color = Color.White,
                    fontSize = 11.sp,
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Deskripsi Album",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = album.description,
            color = Color(0xFFA7A7A7),
            fontSize = 12.sp,
            lineHeight = 18.sp,
            maxLines = if (isDescExpanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = if (isDescExpanded) "Tutup ringkasan" else "Baca selengkapnya",
            color = Color(0xFF1DB954),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onToggleDesc() },
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.MusicNote,
                    contentDescription = null,
                    tint = Color(0xFF1DB954),
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Daftar Lagu",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
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
                    text = "${tracks.size} Lagu",
                    color = Color(0xFF1DB954),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (tracks.isEmpty()) {
            Text(
                text = "Tidak ada daftar lagu untuk album ini.",
                color = Color(0xFFA7A7A7),
                fontSize = 12.sp,
                modifier = Modifier.padding(vertical = 12.dp),
            )
        } else {
            tracks.forEachIndexed { index, track ->
                TrackItem(track = track)
                if (index < tracks.lastIndex) {
                    HorizontalDivider(
                        color = Color(0xFF1E1E1E),
                        thickness = 0.5.dp,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}
