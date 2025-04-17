package com.example.reakageapp.componentsimport androidx.compose.foundation.backgroundimport androidx.compose.foundation.layout.*import androidx.compose.material.icons.Iconsimport androidx.compose.material.icons.filled.ArrowBackimport androidx.compose.material.icons.filled.Searchimport androidx.compose.material3.*import androidx.compose.runtime.Composableimport androidx.compose.ui.Modifierimport androidx.compose.ui.draw.blurimport androidx.compose.ui.graphics.Brushimport androidx.compose.ui.graphics.Colorimport androidx.compose.ui.unit.dpimport androidx.compose.ui.unit.sp@OptIn(ExperimentalMaterial3Api::class)@Composablefun TopBar(    title: String,    showBackButton: Boolean = false,    onBackClick: () -> Unit = {},    onSearchClick: () -> Unit = {}) {    Surface(        modifier = Modifier            .fillMaxWidth()            .height(64.dp)            .blur(12.dp), // glass blur        tonalElevation = 6.dp,        color = Color.White.copy(alpha = 0.1f), // glass transparent background        shadowElevation = 4.dp    ) {        TopAppBar(            title = {                Text(                    text = title.replaceFirstChar { it.uppercase() },                    fontSize = 20.sp,                    color = Color.White,                    modifier = Modifier.fillMaxWidth(),                )            },            navigationIcon = {                if (showBackButton) {                    IconButton(onClick = onBackClick) {                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)                    }                }            },            actions = {                IconButton(onClick = onSearchClick) {                    Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)                }            },            colors = TopAppBarDefaults.topAppBarColors(                containerColor = Color.Transparent // keep the surface glassy            )        )    }}