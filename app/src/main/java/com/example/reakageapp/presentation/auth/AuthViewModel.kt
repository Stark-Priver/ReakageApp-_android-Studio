package com.example.reakageapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class AuthState(
    val isLoading: Boolean = false,
    val user: FirebaseUser? = null,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState(isLoading = true))
    val authState: StateFlow<AuthState> = _authState

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        viewModelScope.launch {
            val currentUser = auth.currentUser
            _authState.value = AuthState(isLoading = false, user = currentUser)
        }
    }

    fun signUpWithEmailPassword(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthState(isLoading = true)
            try {
                val result = auth.createUserWithEmailAndPassword(email, pass).await()
                _authState.value = AuthState(isLoading = false, user = result.user)
            } catch (e: Exception) {
                _authState.value = AuthState(isLoading = false, error = e.message)
            }
        }
    }

    fun signInWithEmailPassword(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthState(isLoading = true)
            try {
                val result = auth.signInWithEmailAndPassword(email, pass).await()
                _authState.value = AuthState(isLoading = false, user = result.user)
            } catch (e: Exception) {
                _authState.value = AuthState(isLoading = false, error = e.message)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            auth.signOut()
            _authState.value = AuthState(isLoading = false, user = null)
        }
    }

    fun clearError() {
        _authState.value = _authState.value.copy(error = null)
    }
}
