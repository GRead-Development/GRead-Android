package com.gread.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gread.data.api.GReadApiService
import com.gread.data.models.Activity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ActivityUiState(
    val activities: List<Activity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val page: Int = 1,
    val hasMore: Boolean = true
)

class ActivityViewModel(private val apiService: GReadApiService) : ViewModel() {
    private val _uiState = MutableStateFlow(ActivityUiState())
    val uiState: StateFlow<ActivityUiState> = _uiState

    init {
        loadActivity()
    }

    fun loadActivity(page: Int = 1) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val feed = apiService.getActivityFeed(page = page)
                val items = feed.items ?: emptyList()
                val newActivities = if (page == 1) items else {
                    (_uiState.value.activities + items)
                }
                _uiState.value = ActivityUiState(
                    activities = newActivities,
                    isLoading = false,
                    page = page,
                    hasMore = (feed.total ?: 0) > newActivities.size
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load activity"
                )
            }
        }
    }

    fun loadMore() {
        val nextPage = _uiState.value.page + 1
        loadActivity(nextPage)
    }
}
