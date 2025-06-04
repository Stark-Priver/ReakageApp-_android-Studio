package com.example.reakageapp.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.reakageapp.theme.AquaLight
import com.example.reakageapp.theme.DeepOcean
import com.example.reakageapp.theme.RippleAccent
import com.example.reakageapp.theme.glassmorphism
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    // Fade-in animation
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000),
        label = "FadeInAnimation"
    )

    // Navigate to HomeScreen after 2 seconds
    LaunchedEffect(Unit) {
        delay(2000) // 2-second delay
        navController.navigate("home") {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(AquaLight, RippleAccent, DeepOcean),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(0f, Float.POSITIVE_INFINITY)
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .glassmorphism()
                .alpha(alpha)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Water-themed image (replace with your app's logo or water-themed drawable)
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery), // Placeholder; replace with water-themed image
                contentDescription = "Reakage App Splash Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(200.dp)
                    .padding(16.dp)
            )
            Text(
                text = "Reakage",
                style = MaterialTheme.typography.displayLarge.copy(
                    color = DeepOcean,
                    fontWeight = FontWeight.Bold,
                    fontSize = 48.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "Your solution for water leak reporting",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = DeepOcean.copy(alpha = 0.85f),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}