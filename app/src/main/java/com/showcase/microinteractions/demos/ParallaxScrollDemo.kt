package com.showcase.microinteractions.demos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.showcase.microinteractions.ui.theme.Coral
import com.showcase.microinteractions.ui.theme.Rose
import com.showcase.microinteractions.ui.theme.Teal
import com.showcase.microinteractions.ui.theme.Violet

private data class Chapter(
    val title: String,
    val body: String
)

private val chapters = listOf(
    Chapter(
        "Why physics reads as quality",
        "A spring settling into place carries information a linear fade never does — it tells " +
            "you something has weight."
    ),
    Chapter(
        "Bounds transitions",
        "Sharing a layout node's position and size across two states lets the system " +
            "interpolate the whole shape, not just its opacity."
    ),
    Chapter(
        "Gesture-driven offsets",
        "Following a finger 1:1 during a drag, then handing off to a spring on release, is " +
            "what makes a swipe feel directly manipulable."
    ),
    Chapter(
        "Depth from mismatched speeds",
        "A background that scrolls slower than the foreground is the same trick a diorama " +
            "uses — layers moving at different rates read as distance."
    ),
    Chapter(
        "Haptics as confirmation",
        "A tap that also produces a tiny pulse of feedback closes the loop faster than a " +
            "visual change alone."
    ),
    Chapter(
        "Restraint",
        "Every demo in this gallery uses exactly one physics idea. Stacking more rarely reads " +
            "as more polished — usually the opposite."
    )
)

private val headerHeight = 300.dp

@Composable
fun ParallaxScrollDemo(onBack: () -> Unit) {
    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(state = listState) {
            item { ParallaxHeader(listState = listState, headerHeight = headerHeight) }
            items(chapters) { chapter -> ChapterCard(chapter) }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 4.dp, vertical = 4.dp)
                .zIndex(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun ParallaxHeader(listState: LazyListState, headerHeight: Dp) {
    val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }

    val scrollProgress by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex == 0) {
                (listState.firstVisibleItemScrollOffset / headerHeightPx).coerceIn(0f, 1f)
            } else {
                1f
            }
        }
    }
    val scrollOffsetPx by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex == 0) {
                listState.firstVisibleItemScrollOffset.toFloat()
            } else {
                headerHeightPx
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerHeight)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    // Background moves at half scroll speed and fades as it exits.
                    translationY = scrollOffsetPx * 0.5f
                    alpha = (1f - scrollProgress).coerceIn(0f, 1f)
                }
                .background(Brush.linearGradient(listOf(Violet, Coral, Teal)))
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
                .graphicsLayer {
                    // Foreground text lags behind the background for a layered depth cue.
                    translationY = scrollOffsetPx * 0.8f
                    alpha = (1f - scrollProgress * 1.4f).coerceIn(0f, 1f)
                }
        ) {
            Text(
                text = "Depth, on scroll",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "A header that moves slower than the list beneath it",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ChapterCard(chapter: Chapter) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(18.dp)
    ) {
        Text(text = chapter.title, style = MaterialTheme.typography.titleMedium)
        Text(
            text = chapter.body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}
