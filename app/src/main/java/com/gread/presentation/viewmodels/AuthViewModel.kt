package com.gread.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gread.data.managers.AuthManager
import com.gread.data.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val isLoggedIn: Boolean = false,
    val error: String? = null
)

class AuthViewModel(private val authManager: AuthManager) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    init {
        viewModelScope.launch {
            // Check if user is already logged in
            val token = authManager.getToken()
            if (token != null) {
                // User has a stored token, mark as logged in
                _uiState.value = AuthUiState(isLoggedIn = true)
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = authManager.login(username, password)
            result.onSuccess { response ->
                _uiState.value = AuthUiState(
                    isLoading = false,
                    user = response.user,
                    isLoggedIn = true
                )
            }
            result.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Login failed"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authManager.logout()
            _uiState.value = AuthUiState()
        }
    }
}
