package com.example.reakageapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.reakageapp.theme.glassmorphism
import com.example.reakageapp.R
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reakageapp.presentation.auth.AuthViewModel

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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

    // Navigate when user is successfully authenticated
    LaunchedEffect(authState.user) {
        if (authState.user != null) {
            // Ensure we clear login/signup from backstack and go to home
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
                // Attempt to pop signup as well, in case user navigated back from signup to login
                // Or if user signed up and was redirected here before full navigation to home
                try {
                    popUpTo("signup") { inclusive = true }
                } catch (e: IllegalStateException) {
                    // Suppress error if "signup" is not on the back stack
                }
            }
        }
    }

    fun handleLogin() {
        if (email.isBlank() || password.isBlank()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Please fill all fields")
            }
            return
        }
        authViewModel.signInWithEmailPassword(email, password)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp)
            ) { data ->
                Snackbar(
                    modifier = Modifier.padding(8.dp),
                    containerColor = MaterialTheme.colorScheme.errorContainer, // Use theme colors
                    contentColor = MaterialTheme.colorScheme.onErrorContainer // Use theme colors
                ) {
                    Text(
                        text = data.visuals.message,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onErrorContainer, // Use theme colors
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
                text = "Login",
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
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Email
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
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Password
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !authState.isLoading
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { handleLogin() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !authState.isLoading
                    ) {
                        if (authState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Login")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate("signup") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary), // Use theme colors
                        enabled = !authState.isLoading
                    ) {
                        Text("Sign Up", color = MaterialTheme.colorScheme.onSecondary) // Use theme colors
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    SocialSignInButtons() // Placeholder, functionality not yet implemented
                }
            }
        }
    }
}

@Composable
fun SocialSignInButtons() {
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
