package com.example.reakageapp.presentation.report

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reakageapp.data.model.Report
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

data class ReportSubmissionState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

data class UserReportsState(
    val isLoading: Boolean = true,
    val reports: List<Report> = emptyList(),
    val error: String? = null
)

class ReportViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("reports")
    private val storage = FirebaseStorage.getInstance().reference.child("report_images")

    private val _submissionState = MutableStateFlow(ReportSubmissionState())
    val submissionState: StateFlow<ReportSubmissionState> = _submissionState

    private val _userReportsState = MutableStateFlow(UserReportsState())
    val userReportsState: StateFlow<UserReportsState> = _userReportsState

    init {
        fetchUserReports()
    }

    fun fetchUserReports() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _userReportsState.value = UserReportsState(isLoading = false, error = "User not authenticated. Cannot fetch reports.")
            return
        }

        _userReportsState.value = UserReportsState(isLoading = true)
        database.orderByChild("userId").equalTo(currentUser.uid)
            .addValueEventListener(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    val reportsList = mutableListOf<Report>()
                    for (reportSnapshot in snapshot.children) {
                        val report = reportSnapshot.getValue(Report::class.java)
                        report?.let {
                            it.reportId = reportSnapshot.key ?: "" // Assign the key as reportId
                            reportsList.add(it)
                        }
                    }
                    _userReportsState.value = UserReportsState(isLoading = false, reports = reportsList.reversed()) // Show newest first
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    _userReportsState.value = UserReportsState(isLoading = false, error = error.message)
                }
            })
    }


    fun submitReport(
        description: String,
        location: String,
        severity: String, // Added severity parameter
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            _submissionState.value = ReportSubmissionState(isLoading = true)

            val currentUser = auth.currentUser
            if (currentUser == null) {
                _submissionState.value = ReportSubmissionState(error = "User not authenticated.")
                return@launch
            }

            try {
                var imageUrl: String? = null
                if (imageUri != null) {
                    // Create a unique path for the image, e.g., userId/imageId.jpg
                    val imageFileName = "${currentUser.uid}/${UUID.randomUUID()}.jpg"
                    val imageRef = storage.child(imageFileName)

                    // Upload image
                    imageRef.putFile(imageUri).await()

                    // Get download URL
                    imageUrl = imageRef.downloadUrl.await().toString()
                }

                val reportId = database.push().key // Generate unique ID for the report
                if (reportId == null) {
                    _submissionState.value = ReportSubmissionState(error = "Failed to generate report ID.")
                    return@launch
                }

                val report = Report(
                    reportId = reportId, // Though we exclude it from DB, good to have it here
                    userId = currentUser.uid,
                    reporterEmail = currentUser.email ?: "N/A",
                    location = location,
                    description = description,
                    photoUrl = imageUrl,
                    // timestamp is handled by ServerValue.TIMESTAMP in the model's default
                    status = "submitted",
                    severity = severity // Added severity
                )

                try {
                    database.child(reportId).setValue(report).await()
                    _submissionState.value = ReportSubmissionState(isSuccess = true)
                } catch (dbError: com.google.firebase.database.DatabaseException) {
                    // More specific error logging for database write failures
                    android.util.Log.e("ReportViewModel", "Firebase Database Error: ${dbError.message}", dbError)
                    _submissionState.value = ReportSubmissionState(error = "Database submission failed: ${dbError.message}")
                }

            } catch (e: Exception) {
                // General error logging
                android.util.Log.e("ReportViewModel", "General Submission Error: ${e.message}", e)
                _submissionState.value = ReportSubmissionState(error = "Submission failed: ${e.message}")
            }
        }
    }

    fun resetSubmissionState() {
        _submissionState.value = ReportSubmissionState()
    }
}
