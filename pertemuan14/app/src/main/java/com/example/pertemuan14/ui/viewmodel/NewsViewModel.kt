package com.example.pertemuan14.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pertemuan14.data.model.Article
import com.example.pertemuan14.data.repository.NewsRepository
import com.example.pertemuan14.ui.state.NewsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private val repository = NewsRepository()

    // --- News list state ---
    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    // --- Currently viewed article ---
    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle = _selectedArticle.asStateFlow()

    // --- Bookmarks ---
    private val _bookmarkedUrls = MutableStateFlow<Set<String>>(emptySet())
    val bookmarkedUrls = _bookmarkedUrls.asStateFlow()

    private val _bookmarkedArticles = MutableStateFlow<List<Article>>(emptyList())
    val bookmarkedArticles = _bookmarkedArticles.asStateFlow()

    // --- Category & Search ---
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val categories = listOf("All", "Technology", "Sports", "Health", "Business", "Entertainment", "Science")

    init {
        loadNews()
    }

    fun loadNews(category: String = "All") {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            try {
                val articles = repository.getTopHeadlines(category)
                _uiState.value = NewsUiState.Success(articles)
            } catch (e: Exception) {
                _uiState.value = NewsUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun searchNews(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            loadNews(_selectedCategory.value)
            return
        }
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            try {
                val articles = repository.searchNews(query)
                _uiState.value = NewsUiState.Success(articles)
            } catch (e: Exception) {
                _uiState.value = NewsUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        _searchQuery.value = ""
        loadNews(category)
    }

    fun selectArticle(article: Article) {
        _selectedArticle.value = article
    }

    fun toggleBookmark(article: Article) {
        val url = article.url
        val currentUrls = _bookmarkedUrls.value.toMutableSet()
        if (url in currentUrls) {
            currentUrls.remove(url)
            _bookmarkedArticles.value = _bookmarkedArticles.value.filter { it.url != url }
        } else {
            currentUrls.add(url)
            _bookmarkedArticles.value = _bookmarkedArticles.value + article
        }
        _bookmarkedUrls.value = currentUrls
    }

    fun isBookmarked(url: String): Boolean = url in _bookmarkedUrls.value

    fun retry() {
        loadNews(_selectedCategory.value)
    }
}
