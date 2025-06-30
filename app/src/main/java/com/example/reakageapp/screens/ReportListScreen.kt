package com.example.reakageapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.reakageapp.data.model.Report // Import the correct Report data class
import com.example.reakageapp.presentation.report.ReportViewModel
import com.example.reakageapp.components.GlassCard
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportListScreen(
    navController: NavController,
    reportViewModel: ReportViewModel = viewModel() // Use the shared ViewModel
) {
    val userReportsState by reportViewModel.userReportsState.collectAsState()

    // Function to format timestamp to a readable date string
    fun formatDate(timestamp: Any): String {
        if (timestamp !is Long || timestamp == 0L) return "N/A"
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0D47A1), Color(0xFF42A5F5))))
    ) {
        GlassCard(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(16.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp) // Reduced padding for more content space
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "My Submitted Reports",
                    style = MaterialTheme.typography.headlineMedium.copy( // Adjusted size
                        color = Color.White, // Assuming GlassCard provides a dark enough backdrop or use a themed color
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                when {
                    userReportsState.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    userReportsState.error != null -> {
                        Text(
                            text = "Error: ${userReportsState.error}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    userReportsState.reports.isEmpty() -> {
                        Text(
                            text = "No reports submitted yet.",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(userReportsState.reports) { report ->
                                ReportListItem(report = report, ::formatDate)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReportListItem(report: Report, formatDate: (Any) -> String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .shadow(4.dp, RoundedCornerShape(12.dp)), // Added shadow for better depth
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)) // Slightly transparent white
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            report.photoUrl?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Report Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp) // Increased height for better image display
                        .clip(RoundedCornerShape(8.dp))
                        .padding(bottom = 8.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = "Description: ${report.description}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Location: ${report.location}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Status: ${report.status}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = when (report.status.lowercase(Locale.getDefault())) {
                        "submitted" -> Color(0xFFFFA000) // Orange
                        "in progress" -> Color(0xFF1976D2) // Blue
                        "resolved" -> Color(0xFF388E3C) // Green
                        else -> Color.Gray
                    }
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Date: ${formatDate(report.timestamp)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            // Add other report details as needed (e.g., reportId for debugging)
            // Text("ID: ${report.reportId}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
