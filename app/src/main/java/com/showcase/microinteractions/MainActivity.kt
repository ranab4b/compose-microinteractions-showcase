package com.showcase.microinteractions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.showcase.microinteractions.demos.ParallaxScrollDemo
import com.showcase.microinteractions.demos.SharedElementDemo
import com.showcase.microinteractions.demos.SpringButtonDemo
import com.showcase.microinteractions.demos.SwipeToRevealDemo
import com.showcase.microinteractions.home.Demo
import com.showcase.microinteractions.home.HomeGridScreen
import com.showcase.microinteractions.ui.theme.MicrointeractionsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MicrointeractionsTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ShowcaseApp()
                }
            }
        }
    }
}

private sealed interface Screen {
    data object Home : Screen
    data class DemoScreen(val demo: Demo) : Screen
}

@Composable
private fun ShowcaseApp() {
    var screen by remember { mutableStateOf<Screen>(Screen.Home) }

    BackHandler(enabled = screen != Screen.Home) {
        screen = Screen.Home
    }

    AnimatedContent(
        targetState = screen,
        label = "root-navigation",
        transitionSpec = {
            fadeIn(tween(220)) togetherWith fadeOut(tween(180))
        }
    ) { target ->
        when (target) {
            is Screen.Home -> HomeGridScreen(
                onDemoClick = { demo -> screen = Screen.DemoScreen(demo) }
            )

            is Screen.DemoScreen -> {
                val onBack = { screen = Screen.Home }
                when (target.demo) {
                    Demo.SHARED_ELEMENT -> SharedElementDemo(onBack = onBack)
                    Demo.SWIPE_TO_REVEAL -> SwipeToRevealDemo(onBack = onBack)
                    Demo.SPRING_BUTTON -> SpringButtonDemo(onBack = onBack)
                    Demo.PARALLAX_SCROLL -> ParallaxScrollDemo(onBack = onBack)
                }
            }
        }
    }
}
