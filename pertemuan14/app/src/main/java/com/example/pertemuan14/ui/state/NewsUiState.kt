package com.example.pertemuan14.ui.state

import com.example.pertemuan14.data.model.Article

sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val articles: List<Article>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}
