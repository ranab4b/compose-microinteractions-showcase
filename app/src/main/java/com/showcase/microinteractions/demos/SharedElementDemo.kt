package com.showcase.microinteractions.demos

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.showcase.microinteractions.ui.theme.Amber
import com.showcase.microinteractions.ui.theme.Coral
import com.showcase.microinteractions.ui.theme.Rose
import com.showcase.microinteractions.ui.theme.Teal
import com.showcase.microinteractions.ui.theme.Violet
import com.showcase.microinteractions.ui.theme.VioletDim
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cyclone
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny

private data class Record(
    val id: Int,
    val title: String,
    val artist: String,
    val description: String,
    val icon: ImageVector,
    val gradient: List<Color>
)

private val albums = listOf(
    Record(
        id = 1,
        title = "Nebula Drift",
        artist = "Kaia Voss",
        description = "A slow-burning ambient set built from modular synth patches recorded " +
            "over three nights in an empty planetarium.",
        icon = Icons.Filled.Cyclone,
        gradient = listOf(Violet, Rose)
    ),
    Record(
        id = 2,
        title = "Monsoon Static",
        artist = "Field & Rye",
        description = "Field recordings of the first rains, layered under a rhythm section " +
            "that never quite resolves.",
        icon = Icons.Filled.WaterDrop,
        gradient = listOf(Teal, VioletDim)
    ),
    Record(
        id = 3,
        title = "Ember Season",
        artist = "Low Hallow",
        description = "Warm analog tape hiss, brass swells, and a chorus that only shows up " +
            "once, right at the end.",
        icon = Icons.Filled.Bolt,
        gradient = listOf(Coral, Amber)
    ),
    Record(
        id = 4,
        title = "Glacier Hours",
        artist = "Pale Static",
        description = "Minimal piano sketches recorded at 4am, mixed to sound like the room " +
            "is bigger than it is.",
        icon = Filled.Album,
        gradient = listOf(VioletDim, Teal)
    ),
    Record(
        id = 5,
        title = "Solstice Choir",
        artist = "Wide Aperture",
        description = "A single vocal take, granular-synthesized into a hundred overlapping " +
            "harmonies.",
        icon = Icons.Filled.WbSunny,
        gradient = listOf(Amber, Rose)
    ),
    Record(
        id = 6,
        title = "Aftertide",
        artist = "Kaia Voss",
        description = "The quiet companion record to Nebula Drift — same room, no synths, " +
            "just the piano and the rain outside.",
        icon = Icons.Filled.Nightlight,
        gradient = listOf(Rose, VioletDim)
    )
)

@OptIn(ExperimentalSharedTransitionApi::class)
private val albumBoundsTransform = BoundsTransform { _, _ ->
    spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedElementDemo(onBack: () -> Unit) {
    var selected by remember { mutableStateOf<Record?>(null) }

    SharedTransitionLayout {
        AnimatedContent(
            targetState = selected,
            label = "sharedElementDemo",
            transitionSpec = {
                fadeIn(tween(200)) togetherWith fadeOut(tween(150))
            }
        ) { target ->
            if (target == null) {
                AlbumList(
                    sharedScope = this@SharedTransitionLayout,
                    animatedScope = this@AnimatedContent,
                    onAlbumClick = { selected = it },
                    onBack = onBack
                )
            } else {
                AlbumDetail(
                    album = target,
                    sharedScope = this@SharedTransitionLayout,
                    animatedScope = this@AnimatedContent,
                    onClose = { selected = null }
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun AlbumList(
    sharedScope: SharedTransitionScope,
    animatedScope: AnimatedVisibilityScope,
    onAlbumClick: (Record) -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Column(modifier = Modifier.padding(start = 4.dp)) {
                Text("Shared Element", style = MaterialTheme.typography.titleLarge)
                Text(
                    "Tap a record to expand it",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(albums, key = { it.id }) { album ->
                with(sharedScope) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .sharedBounds(
                                rememberSharedContentState(key = "container-${album.id}"),
                                animatedVisibilityScope = animatedScope,
                                boundsTransform = albumBoundsTransform,
                                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                            )
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { onAlbumClick(album) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .sharedElement(
                                    rememberSharedContentState(key = "art-${album.id}"),
                                    animatedVisibilityScope = animatedScope
                                )
                                .clip(RoundedCornerShape(14.dp))
                                .background(Brush.linearGradient(album.gradient)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(album.icon, contentDescription = null, tint = Color.White)
                        }

                        Spacer(modifier = Modifier.size(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = album.title,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.sharedBounds(
                                    rememberSharedContentState(key = "title-${album.id}"),
                                    animatedVisibilityScope = animatedScope
                                )
                            )
                            Text(
                                text = album.artist,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun AlbumDetail(
    album: Record,
    sharedScope: SharedTransitionScope,
    animatedScope: AnimatedVisibilityScope,
    onClose: () -> Unit
) {
    with(sharedScope) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .sharedBounds(
                    rememberSharedContentState(key = "container-${album.id}"),
                    animatedVisibilityScope = animatedScope,
                    boundsTransform = albumBoundsTransform,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                )
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .sharedElement(
                        rememberSharedContentState(key = "art-${album.id}"),
                        animatedVisibilityScope = animatedScope
                    )
                    .background(Brush.linearGradient(album.gradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = album.icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(72.dp)
                )

                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .statusBarsPadding()
                        .padding(8.dp)
                ) {
                    Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.White)
                }
            }

            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = album.title,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.sharedBounds(
                        rememberSharedContentState(key = "title-${album.id}"),
                        animatedVisibilityScope = animatedScope
                    )
                )
                Text(
                    text = album.artist,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.size(20.dp))
                Text(
                    text = album.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
