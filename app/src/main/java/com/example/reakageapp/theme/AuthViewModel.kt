package com.example.reakageapp.ui.viewmodel // Adjust package name if needed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
    object Loading : AuthResult()
    object Idle : AuthResult() // Initial state
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _loginResult = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val loginResult: StateFlow<AuthResult> = _loginResult.asStateFlow()

    fun loginUser(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginResult.value = AuthResult.Error("Email and password cannot be empty.")
            return
        }

        viewModelScope.launch {
            _loginResult.value = AuthResult.Loading
            try {
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
                _loginResult.value = AuthResult.Success
            } catch (e: FirebaseAuthInvalidUserException) {
                _loginResult.value = AuthResult.Error("Login failed: User not found or incorrect email.")
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _loginResult.value = AuthResult.Error("Login failed: Incorrect password.")
            } catch (e: Exception) {
                _loginResult.value = AuthResult.Error("Login failed: ${e.localizedMessage ?: "Unknown error"}")
                e.printStackTrace() // For developer debugging
            }
        }
    }

    fun resetAuthState() {
        _loginResult.value = AuthResult.Idle
    }
}