package com.gread.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gread.data.api.GReadApiService
import com.gread.data.models.User
import com.gread.data.models.UserStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UserUiState(
    val user: User? = null,
    val stats: UserStats? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class UserViewModel(private val apiService: GReadApiService) : ViewModel() {
    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState

    fun loadUser(userId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val user = apiService.getUser(userId)
                val stats = try {
                    apiService.getUserStats(userId)
                } catch (e: Exception) {
                    null
                }
                _uiState.value = UserUiState(
                    user = user,
                    stats = stats,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load user"
                )
            }
        }
    }
}
