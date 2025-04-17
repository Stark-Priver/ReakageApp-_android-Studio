package com.example.reakageapp.themeimport androidx.compose.material3.*import androidx.compose.runtime.Composableimport androidx.compose.ui.graphics.Color// Custom Colors for Glassmorphism and Blue Themeval BlueLight = Color(0xFF4C83FF) // Light Blue for backgroundval BlueDark = Color(0xFF0066CC)  // Dark Blue for text and accentsval White = Color(0xFFFFFFFF)     // White color for general backgroundval TranslucentWhite = Color(0x80FFFFFF) // Semi-transparent white for glassmorphismprivate val LightColorScheme = lightColorScheme(    primary = BlueDark,    secondary = BlueLight,    background = Color(0xFFE3F2FD),    surface = TranslucentWhite.copy(alpha = 0.4f), // Apply a translucent effect for surfaces    onPrimary = Color.White,    onSecondary = Color.Black,    onBackground = Color.Black,    onSurface = Color.Black)@Composablefun ReakageAppTheme(content: @Composable () -> Unit) {    MaterialTheme(        colorScheme = LightColorScheme, // Applying updated color scheme        typography = Typography(),        content = content    )}