package com.example.reakageapp.themeimport androidx.compose.material3.*import androidx.compose.runtime.Composableimport androidx.compose.ui.graphics.Colorprivate val LightColorScheme = lightColorScheme(    primary = Color(0xFF1565C0),    secondary = Color(0xFF5E92F3),    background = Color(0xFFE3F2FD),    surface = Color(0x40FFFFFF),    onPrimary = Color.White,    onSecondary = Color.Black,    onBackground = Color.Black,    onSurface = Color.Black)@Composablefun ReakageAppTheme(content: @Composable () -> Unit) {    MaterialTheme(        colorScheme = LightColorScheme,        typography = Typography(),        content = content    )}