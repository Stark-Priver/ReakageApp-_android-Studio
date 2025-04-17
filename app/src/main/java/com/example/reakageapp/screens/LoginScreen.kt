package com.example.reakageapp.ui.screensimport androidx.compose.foundation.layout.*import androidx.compose.ui.text.input.PasswordVisualTransformationimport androidx.compose.material3.*import androidx.compose.runtime.*import androidx.compose.ui.Alignmentimport androidx.compose.ui.Modifierimport androidx.compose.ui.unit.dpimport androidx.navigation.NavController@Composablefun LoginScreen(navController: NavController) {    var email by remember { mutableStateOf("") }    var password by remember { mutableStateOf("") }    Column(        modifier = Modifier.fillMaxSize().padding(24.dp),        verticalArrangement = Arrangement.Center,        horizontalAlignment = Alignment.CenterHorizontally    ) {        Text("Login", style = MaterialTheme.typography.headlineMedium)        Spacer(modifier = Modifier.height(16.dp))        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })        Spacer(modifier = Modifier.height(8.dp))        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation())        Spacer(modifier = Modifier.height(16.dp))        Button(onClick = { navController.navigate("home") }) {            Text("Login")        }        Spacer(modifier = Modifier.height(8.dp))        TextButton(onClick = { navController.navigate("signup") }) {            Text("Don't have an account? Sign Up")        }    }}