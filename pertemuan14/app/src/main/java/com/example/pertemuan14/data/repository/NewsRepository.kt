package com.example.pertemuan14.data.repository

import com.example.pertemuan14.data.model.Article
import com.example.pertemuan14.data.remote.RetrofitClient

class NewsRepository {

    private val apiKey = "6c817c0a401f4c03aff81112ba80b56c"

    suspend fun getTopHeadlines(category: String = ""): List<Article> {
        val response = if (category.isBlank() || category == "All") {
            RetrofitClient.apiService.getTopHeadlines(
                country = "us",
                apiKey = apiKey
            )
        } else {
            RetrofitClient.apiService.getTopHeadlines(
                country = "us",
                category = category.lowercase(),
                apiKey = apiKey
            )
        }
        return response.articles.filter { it.title != "[Removed]" && it.title.isNotBlank() }
    }

    suspend fun searchNews(query: String): List<Article> {
        val response = RetrofitClient.apiService.searchNews(
            query = query,
            apiKey = apiKey
        )
        return response.articles.filter { it.title != "[Removed]" && it.title.isNotBlank() }
    }
}
