package com.example.reakageapp.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.reakageapp.components.GlassCard
import com.example.reakageapp.presentation.auth.AuthViewModel
import com.example.reakageapp.theme.AquaLight
import com.example.reakageapp.theme.DeepOcean
import com.example.reakageapp.theme.RippleAccent
import com.example.reakageapp.theme.glassmorphism
import com.example.reakageapp.R // Assuming R.drawable.ic_menu_gallery exists or replace

// User data class (can be expanded later to fetch from Firebase)
data class UserProfileData(
    val name: String,
    val email: String, // This will come from Firebase Auth
    val phone: String,
    val imageResId: Int? = R.drawable.water // Placeholder
)

@Composable
fun ProfileScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val authState by authViewModel.authState.collectAsState()
    val firebaseUser = authState.user

    // State for edit mode and local user details (can be expanded for Firebase profile updates)
    var isEditing by remember { mutableStateOf(false) }

    // Initialize user details, using Firebase email if available
    var name by remember(firebaseUser) { mutableStateOf(firebaseUser?.displayName ?: "User Name") } // Or fetch from DB
    var emailDisplay by remember(firebaseUser) { mutableStateOf(firebaseUser?.email ?: "loading...") }
    var phone by remember { mutableStateOf("123-456-7890") } // Mock phone

    // Update emailDisplay when authState changes and user is available
    LaunchedEffect(firebaseUser) {
        emailDisplay = firebaseUser?.email ?: "Not logged in"
        // You could also fetch and update 'name' and 'phone' here if they were stored in Firebase
    }

    // Navigate to login screen if user becomes null (logged out)
    LaunchedEffect(firebaseUser, navController) {
        if (firebaseUser == null && !authState.isLoading) { // Ensure not in midst of loading state
             // Check current route to prevent navigation if already on a non-auth screen or during initial composition
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            if (currentRoute != "login" && currentRoute != "signup" && currentRoute != "splash") {
                navController.navigate("login") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }


    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 800),
        label = "FadeInAnimation"
    )

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
        GlassCard(
            modifier = Modifier
                .fillMaxSize()
                .glassmorphism()
                .align(Alignment.Center)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.water), // Replace with actual user avatar if available
                        contentDescription = "User avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(2.dp, RippleAccent, CircleShape)
                            .padding(4.dp)
                    )
                    Text(
                        text = "My Profile",
                        style = MaterialTheme.typography.displayMedium.copy(
                            color = DeepOcean,
                            fontSize = 36.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isEditing) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Name") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors( /* ... */ )
                        )
                        OutlinedTextField(
                            value = emailDisplay, // Email is generally not editable here
                            onValueChange = { /* emailDisplay = it */ },
                            label = { Text("Email") },
                            readOnly = true, // Email from auth provider
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors( /* ... */ )
                        )
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors( /* ... */ )
                        )
                    } else {
                        Text("Name: $name", style = MaterialTheme.typography.bodyLarge.copy(color = DeepOcean, fontSize = 18.sp))
                        Text("Email: $emailDisplay", style = MaterialTheme.typography.bodyLarge.copy(color = DeepOcean, fontSize = 18.sp))
                        Text("Phone: $phone", style = MaterialTheme.typography.bodyLarge.copy(color = DeepOcean, fontSize = 18.sp))
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isEditing) {
                        Button(
                            onClick = {
                                // TODO: Implement saving profile changes to Firebase (e.g., Firestore user profile collection)
                                // For now, just updates local state:
                                // firebaseUser?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())
                                isEditing = false
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RippleAccent)
                        ) { Text("Save Changes", fontSize = 16.sp) }
                    }
                    Button(
                        onClick = { isEditing = !isEditing },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepOcean)
                    ) { Text(if (isEditing) "Cancel" else "Update Profile", fontSize = 16.sp) }

                    Button(
                        onClick = {
                            authViewModel.signOut()
                            // Navigation is handled by LaunchedEffect observing authState.user
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AquaLight.copy(alpha = 0.9f))
                    ) { Text("Logout", fontSize = 16.sp) }

                    Button(
                        onClick = { navController.navigate("home") },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AquaLight)
                    ) { Text("Back to Home", fontSize = 16.sp) }
                }
            }
        }
    }
}
