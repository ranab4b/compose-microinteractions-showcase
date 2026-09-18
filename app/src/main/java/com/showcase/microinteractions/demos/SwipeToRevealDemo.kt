package com.showcase.microinteractions.demos

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.showcase.microinteractions.ui.theme.Amber
import com.showcase.microinteractions.ui.theme.DangerRed
import com.showcase.microinteractions.ui.theme.Teal
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private data class InboxMessage(
    val id: Int,
    val sender: String,
    val preview: String,
    val timestamp: String
)

private fun defaultMessages() = mutableStateListOf(
    InboxMessage(1, "Priya Shah", "The updated deck is in the shared folder now.", "9:41 AM"),
    InboxMessage(2, "Design Weekly", "5 motion patterns worth stealing this week.", "8:15 AM"),
    InboxMessage(3, "Marcus Chen", "Can we push the sync to Thursday?", "Yesterday"),
    InboxMessage(4, "Release Bot", "Build #482 passed all checks.", "Yesterday"),
    InboxMessage(5, "Priya Shah", "Left a couple of comments on the prototype.", "Mon"),
    InboxMessage(6, "Ana Torres", "Loved the swipe interaction, ship it.", "Mon"),
    InboxMessage(7, "Design Weekly", "Spring vs. tween: when to use which.", "Sun")
)

private val actionWidth = 72.dp

@Composable
fun SwipeToRevealDemo(onBack: () -> Unit) {
    val messages = remember { defaultMessages() }

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
                Text("Swipe to Reveal", style = MaterialTheme.typography.titleLarge)
                Text(
                    "Drag a message left to archive or delete",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        LazyColumn {
            items(messages, key = { it.id }) { message ->
                SwipeRevealRow(
                    message = message,
                    modifier = Modifier.animateItem(),
                    onArchive = { messages.remove(message) },
                    onDelete = { messages.remove(message) }
                )
            }
        }
    }
}

@Composable
private fun SwipeRevealRow(
    message: InboxMessage,
    modifier: Modifier = Modifier,
    onArchive: () -> Unit,
    onDelete: () -> Unit
) {
    val density = LocalDensity.current
    val revealPx = with(density) { (actionWidth * 2).toPx() }
    val flyOffPx = with(density) { 480.dp.toPx() }

    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val draggableState = rememberDraggableState { delta ->
        scope.launch {
            offsetX.snapTo((offsetX.value + delta).coerceIn(-revealPx, 0f))
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(78.dp)
    ) {
        Row(
            modifier = Modifier
                .matchParentSize(),
            horizontalArrangement = Arrangement.End
        ) {
            RevealAction(
                icon = Icons.Filled.Archive,
                background = Teal,
                onClick = {
                    scope.launch {
                        offsetX.animateTo(0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                        onArchive()
                    }
                }
            )
            RevealAction(
                icon = Icons.Filled.Delete,
                background = DangerRed,
                onClick = {
                    scope.launch {
                        offsetX.animateTo(-flyOffPx, tween(220))
                        onDelete()
                    }
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .background(MaterialTheme.colorScheme.background)
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    onDragStopped = { _ ->
                        val target = if (offsetX.value < -revealPx / 2f) -revealPx else 0f
                        offsetX.animateTo(
                            targetValue = target,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        )
                    }
                )
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Amber),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message.sender.take(1),
                    color = Color.White,
                    fontSize = 18.sp
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 14.dp)
                    .weight(1f)
            ) {
                Text(text = message.sender, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = message.preview,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }

            Text(
                text = message.timestamp,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RevealAction(
    icon: ImageVector,
    background: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(actionWidth)
            .background(background)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = Color.White)
    }
}
