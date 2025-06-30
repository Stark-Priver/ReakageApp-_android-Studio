package com.example.reakageapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.reakageapp.components.BottomNavigationBar
import com.example.reakageapp.presentation.auth.AuthViewModel
import com.example.reakageapp.presentation.report.ReportViewModel // Import ReportViewModel
import com.example.reakageapp.screens.HomeScreen
import com.example.reakageapp.screens.LoginScreen
import com.example.reakageapp.screens.ProfileScreen
import com.example.reakageapp.screens.ReportListScreen
import com.example.reakageapp.screens.ReportScreen // Assuming this is a detail screen, not submission
import com.example.reakageapp.screens.SignUpScreen
import com.example.reakageapp.screens.SplashScreen
import com.example.reakageapp.screens.SubmitReportScreen // Import SubmitReportScreen
import com.example.reakageapp.theme.ReakageAppTheme

@Composable
fun NavigationGraph(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val reportViewModel: ReportViewModel = viewModel() // Instantiate ReportViewModel

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf("home", "reports", "profile") // "report" might be detail, "submit_report" is new

    ReakageAppTheme {
        Scaffold(
            bottomBar = {
                if (showBottomBar) BottomNavigationBar(navController = navController)
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "splash",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("splash") { SplashScreen(navController, authViewModel) }
                composable("login") { LoginScreen(navController, authViewModel) }
                composable("signup") { SignUpScreen(navController, authViewModel) }

                // Authenticated routes
                composable("home") { HomeScreen(navController) }
                composable("submit_report") { SubmitReportScreen(navController, reportViewModel) } // Added route
                composable("report") { ReportScreen(navController) } // Assuming this is for viewing a single report's details
                composable("reports") { ReportListScreen(navController) } // For listing reports
                composable("profile") { ProfileScreen(navController, authViewModel) }
            }
        }
    }
}
