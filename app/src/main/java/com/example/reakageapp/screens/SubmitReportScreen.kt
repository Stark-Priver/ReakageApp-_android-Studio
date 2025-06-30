@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.reakageapp.screens

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.reakageapp.presentation.report.ReportViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SubmitReportScreen(
    navController: NavController,
    reportViewModel: ReportViewModel = viewModel()
) {
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val severityOptions = listOf("Low", "Medium", "High")
    var selectedSeverity by remember { mutableStateOf(severityOptions[0]) }
    var expandedSeverityDropdown by remember { mutableStateOf(false) }

    val submissionState by reportViewModel.submissionState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Permission for reading images
    // For Android 13+ (API 33+), use READ_MEDIA_IMAGES. For older, use READ_EXTERNAL_STORAGE.
    val readPermission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    val readPermissionState = rememberPermissionState(permission = readPermission)

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    LaunchedEffect(submissionState) {
        if (submissionState.isSuccess) {
            snackbarHostState.showSnackbar("Report submitted successfully!")
            reportViewModel.resetSubmissionState()
            imageUri = null
            description = ""
            location = ""
            // Optionally navigate back or to a list screen
            // navController.popBackStack()
        }
        submissionState.error?.let {
            snackbarHostState.showSnackbar("Error: $it")
            reportViewModel.resetSubmissionState()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Submit New Report") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description of Issue") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5
            )

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location (e.g., Address or Landmark)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Severity Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedSeverityDropdown,
                onExpandedChange = { expandedSeverityDropdown = !expandedSeverityDropdown },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedSeverity,
                    onValueChange = {}, // Not directly changing here
                    readOnly = true,
                    label = { Text("Severity") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSeverityDropdown) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedSeverityDropdown,
                    onDismissRequest = { expandedSeverityDropdown = false }
                ) {
                    severityOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedSeverity = selectionOption
                                expandedSeverityDropdown = false
                            }
                        )
                    }
                }
            }

            Button(onClick = {
                if (readPermissionState.status.isGranted) {
                    imagePickerLauncher.launch("image/*")
                } else {
                    readPermissionState.launchPermissionRequest()
                }
            }) {
                Text("Select Image (Optional)")
            }

            imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(model = it),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 8.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (description.isNotBlank() && location.isNotBlank()) {
                        reportViewModel.submitReport(description, location, selectedSeverity, imageUri)
                    } else {
                        // Show snackbar for empty fields
                        // This can be done by setting an error in a local state or viewmodel
                        // For now, let's assume user fills them.
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !submissionState.isLoading
            ) {
                if (submissionState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Submit Report")
                }
            }
        }
    }
}
