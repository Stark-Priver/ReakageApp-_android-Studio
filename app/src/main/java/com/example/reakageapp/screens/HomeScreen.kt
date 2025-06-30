package com.example.reakageapp.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.reakageapp.R
import com.example.reakageapp.components.GlassCard
import com.example.reakageapp.theme.AquaLight
import com.example.reakageapp.theme.DeepOcean
import com.example.reakageapp.theme.RippleAccent
import com.example.reakageapp.theme.glassmorphism

@OptIn(ExperimentalMaterial3Api::class) // For Scaffold
@Composable
fun HomeScreen(navController: NavController) {
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing),
        label = "FadeInAnimation"
    )

    // Using Scaffold to potentially add a FAB or TopAppBar later if needed,
    // though current design is full-screen GlassCard.
    // For now, the main "Report Water Issue" button will navigate.
    Scaffold(
        // Example FAB if you wanted one instead of modifying the button:
        // floatingActionButton = {
        //     FloatingActionButton(onClick = { navController.navigate("submit_report") }) {
        //         Icon(Icons.Filled.Add, "Submit new report")
        //     }
        // }
    ) { paddingValues -> // paddingValues from Scaffold should be applied if Scaffold has Top/Bottom bars
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
                .padding(paddingValues) // Apply padding from Scaffold
                .padding(16.dp) // Original padding
        ) {
            GlassCard(
                modifier = Modifier
                    .fillMaxSize()
                    .glassmorphism()
                    .alpha(alpha)
                    .align(Alignment.Center)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp), // Inner padding for GlassCard content
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Welcome to Water Watch",
                            style = MaterialTheme.typography.displayMedium.copy(
                                color = DeepOcean,
                                fontWeight = FontWeight.Bold,
                                fontSize = 36.sp
                            ),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Report water leaks and quality problems easily.",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = DeepOcean.copy(alpha = 0.85f),
                                fontSize = 20.sp,
                                lineHeight = 28.sp
                            ),
                            modifier = Modifier.padding(horizontal = 16.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    Image(
                        painter = painterResource(id = R.drawable.water),
                        contentDescription = "Water droplet icon",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(200.dp)
                            .padding(16.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { navController.navigate("submit_report") }, // Changed navigation
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DeepOcean,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Text(
                                text = "Report Water Issue",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Button(
                            onClick = { navController.navigate("reports") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RippleAccent,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Text(
                                text = "View My Reports", // Clarified button text
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Button(
                            onClick = { navController.navigate("profile") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AquaLight,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Text(
                                text = "My Profile",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
