package com.example.reakageapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.reakageapp.R
import com.example.reakageapp.presentation.auth.AuthViewModel
import com.example.reakageapp.theme.glassmorphism
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val authState by authViewModel.authState.collectAsState()

    // Show snackbar for errors from ViewModel
    LaunchedEffect(authState.error) {
        authState.error?.let {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(it)
                authViewModel.clearError() // Clear error after showing
            }
        }
    }

    // Navigate when user is successfully authenticated (signing up also logs them in)
    LaunchedEffect(authState.user) {
        if (authState.user != null) {
            navController.navigate("home") {
                popUpTo("signup") { inclusive = true } // Clear signup from back stack
                // Attempt to pop login as well, in case user navigated from login to signup
                try {
                    popUpTo("login") { inclusive = true }
                } catch (e: IllegalStateException) {
                    // Suppress error if "login" is not on the back stack
                }
            }
        }
    }

    fun handleSignUp() {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            coroutineScope.launch { snackbarHostState.showSnackbar("Please fill all fields") }
            return
        }
        if (password != confirmPassword) {
            coroutineScope.launch { snackbarHostState.showSnackbar("Passwords do not match") }
            return
        }
        authViewModel.signUpWithEmailPassword(email, password)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState, modifier = Modifier.padding(16.dp)) { data ->
                Snackbar(
                    modifier = Modifier.padding(8.dp),
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ) {
                    Text(
                        text = data.visuals.message,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontSize = 16.sp
                        )
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            Box(
                modifier = Modifier
                    .glassmorphism()
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !authState.isLoading
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !authState.isLoading
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !authState.isLoading
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { handleSignUp() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !authState.isLoading
                    ) {
                        if (authState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Sign Up")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // SocialSignInButtons() // Placeholder from original, can be added back if needed
                    // Already have a login screen, so no need for "already have account" button usually
                    // But if desired, a TextButton to navigate to Login can be added here.
                    TextButton(onClick = { navController.navigate("login") { popUpTo("signup") {inclusive = true} } }) {
                        Text("Already have an account? Login")
                    }
                }
            }
        }
    }
}

// Assuming SocialSignInButtons is defined elsewhere or remove if not used on this screen.
// If it was defined in the old SignUpScreen.kt and is needed, it should be here.
// For now, I'm omitting it as it was in LoginScreen.kt and might be a shared component.
// Re-adding a simplified version from LoginScreen.kt for completeness if it was intended to be here too.
@Composable
fun SocialSignInButtons() { // This was in LoginScreen, if needed here, ensure R.drawable resources exist
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* TODO: Handle Google sign-in */ }) {
            Image(painter = painterResource(id = R.drawable.google_logo), contentDescription = "Google SignIn")
        }
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(onClick = { /* TODO: Handle Facebook sign-in */ }) {
            Image(painter = painterResource(id = R.drawable.facebook_logo), contentDescription = "Facebook SignIn")
        }
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(onClick = { /* TODO: Handle Instagram sign-in */ }) {
            Image(painter = painterResource(id = R.drawable.instagram_logo), contentDescription = "Instagram SignIn")
        }
    }
}
