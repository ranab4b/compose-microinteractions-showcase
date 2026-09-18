package com.showcase.microinteractions.home

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.SwipeLeft
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.showcase.microinteractions.ui.theme.Coral
import com.showcase.microinteractions.ui.theme.Rose
import com.showcase.microinteractions.ui.theme.Teal
import com.showcase.microinteractions.ui.theme.Violet

enum class Demo(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val gradient: List<Color>
) {
    SHARED_ELEMENT(
        title = "Shared Element",
        subtitle = "List → detail bounds transition",
        icon = Icons.Filled.ViewCarousel,
        gradient = listOf(Violet, Rose)
    ),
    SWIPE_TO_REVEAL(
        title = "Swipe to Reveal",
        subtitle = "Draggable actions with spring settle",
        icon = Icons.Filled.SwipeLeft,
        gradient = listOf(Teal, Violet)
    ),
    SPRING_BUTTON(
        title = "Spring Button",
        subtitle = "Bouncy press feedback + haptics",
        icon = Icons.Filled.TouchApp,
        gradient = listOf(Coral, Rose)
    ),
    PARALLAX_SCROLL(
        title = "Parallax Scroll",
        subtitle = "Scroll-linked header depth",
        icon = Icons.Filled.Layers,
        gradient = listOf(Rose, Teal)
    )
}

@Composable
fun HomeGridScreen(onDemoClick: (Demo) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Text(
                text = "Micro-interactions",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Four small, physics-based Compose demos.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(Demo.entries) { demo ->
                DemoCard(demo = demo, onClick = { onDemoClick(demo) })
            }
        }
    }
}

@Composable
private fun DemoCard(demo: Demo, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "demoCardScale"
    )

    Column(
        modifier = Modifier
            .aspectRatio(0.92f)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.linearGradient(demo.gradient))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.18f))
                .padding(10.dp)
        ) {
            Icon(
                imageVector = demo.icon,
                contentDescription = null,
                tint = Color.White
            )
        }

        Column {
            Text(
                text = demo.title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = demo.subtitle,
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
