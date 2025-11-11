package com.gread.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gread.data.api.GReadApiService
import com.gread.data.models.Group
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class GroupsUiState(
    val groups: List<Group> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val page: Int = 1,
    val hasMore: Boolean = true
)

class GroupsViewModel(private val apiService: GReadApiService) : ViewModel() {
    private val _uiState = MutableStateFlow(GroupsUiState())
    val uiState: StateFlow<GroupsUiState> = _uiState

    init {
        loadGroups()
    }

    fun loadGroups(page: Int = 1) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val response = apiService.getGroups(page = page)
                val newGroups = if (page == 1) response.items ?: emptyList() else {
                    (_uiState.value.groups + (response.items ?: emptyList()))
                }
                _uiState.value = GroupsUiState(
                    groups = newGroups,
                    isLoading = false,
                    page = page,
                    hasMore = (response.total ?: 0) > newGroups.size
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load groups"
                )
            }
        }
    }

    fun loadMore() {
        val nextPage = _uiState.value.page + 1
        loadGroups(nextPage)
    }
}
