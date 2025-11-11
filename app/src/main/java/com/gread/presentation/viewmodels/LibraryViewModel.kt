package com.gread.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gread.data.api.GReadApiService
import com.gread.data.models.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LibraryUiState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val page: Int = 1,
    val hasMore: Boolean = true
)

class LibraryViewModel(private val apiService: GReadApiService) : ViewModel() {
    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState

    init {
        loadLibrary()
    }

    fun loadLibrary(page: Int = 1) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val response = apiService.getLibrary(page = page)
                val newBooks = if (page == 1) response.items ?: emptyList() else {
                    (_uiState.value.books + (response.items ?: emptyList()))
                }
                _uiState.value = LibraryUiState(
                    books = newBooks,
                    isLoading = false,
                    page = page,
                    hasMore = (response.total ?: 0) > newBooks.size
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load library"
                )
            }
        }
    }

    fun loadMore() {
        val nextPage = _uiState.value.page + 1
        loadLibrary(nextPage)
    }
}
