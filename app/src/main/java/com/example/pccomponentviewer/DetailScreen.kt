package com.example.pccomponentviewer

import android.R
import android.R.attr.maxWidth
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.pccomponentviewer.data.GraphicsCard
import com.example.pccomponentviewer.data.PCComponent
import com.example.pccomponentviewer.data.RAM
import com.example.pccomponentviewer.data.Storage
import com.example.pccomponentviewer.data.StorageType
import com.google.common.reflect.Reflection.getPackageName


class DetailScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val player = ExoPlayer.Builder(this).build()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    pcComponent: PCComponent,
    onNavigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pcComponent.name) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Powrót"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
            ,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PCComponentIcon(
                pcComponentIcon = pcComponent.imageResourceId,
                modifier = Modifier
                    .size(200.dp)
                    .clip(MaterialTheme.shapes.large),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = pcComponent.name,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            when (pcComponent) {
                is RAM -> {
                    SpecificationItem("Producent", pcComponent.manufacturer)
                    SpecificationItem("Rozmiar", "${pcComponent.size} GB")
                    SpecificationItem("Typ", pcComponent.type.name)
                    SpecificationItem("Taktowanie", "${pcComponent.frequency} MHz")
                    SpecificationItem("Opóźnienia", pcComponent.timing)
                }

                is Storage -> {
                    SpecificationItem("Producent", pcComponent.manufacturer)
                    SpecificationItem("Typ", pcComponent.type.name.replace("_", " "))
                    SpecificationItem("Pojemność", "${pcComponent.capacity} GB")
                    when (pcComponent.type) {
                        StorageType.HDD -> {
                            SpecificationItem("Obroty", "${pcComponent.rpm} RPM")
                        }

                        else -> {
                            SpecificationItem("Prędkość odczytu", "${pcComponent.readSpeed} MB/s")
                            SpecificationItem("Prędkość zapisu", "${pcComponent.writeSpeed} MB/s")
                        }
                    }
                }

                is GraphicsCard -> {
                    SpecificationItem("Producent", pcComponent.manufacturer)
                    SpecificationItem("Pamięć VRAM", "${pcComponent.vramSize} GB")
                    SpecificationItem("Taktowanie", "${pcComponent.clockSpeed} MHz")
                }
            }
            if(pcComponent.videoUrl != null) {
                VideoPlayerItem(videoUri = pcComponent.videoUrl!!)
            }
        }
    }
}

@Composable
fun SpecificationItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayerItem(videoUri: String) {
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri("android.resource://${context.packageName}/raw/${videoUri}"))
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f/9f),
        factory = { context ->
            PlayerView(context).apply {
                this.player = player
            }
        }
    )
}

private fun Int.toDp() {
    TODO("Not yet implemented")
}
