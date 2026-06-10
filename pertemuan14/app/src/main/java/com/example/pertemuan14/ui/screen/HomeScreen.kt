package com.example.pertemuan14.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pertemuan14.data.model.Article
import com.example.pertemuan14.ui.component.CategoryChipRow
import com.example.pertemuan14.ui.component.NewsCard
import com.example.pertemuan14.ui.state.NewsUiState
import com.example.pertemuan14.ui.theme.NewsBlack
import com.example.pertemuan14.ui.theme.NewsDarkSurface
import com.example.pertemuan14.ui.theme.NewsGray
import com.example.pertemuan14.ui.theme.NewsWhite
import com.example.pertemuan14.ui.viewmodel.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: NewsViewModel,
    onArticleClick: (Article) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val categories = viewModel.categories

    // rememberSaveable keeps the search text across recompositions & config changes
    var searchText by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NewsBlack)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Trending",
                    color = NewsWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = NewsBlack
            )
        )

        // Search Bar
        TextField(
            value = searchText,
            onValueChange = { newValue ->
                searchText = newValue
                // Live search: clear results if field emptied
                if (newValue.isBlank()) {
                    viewModel.searchNews("")
                }
            },
            placeholder = {
                Text("Search articles...", color = NewsGray, fontSize = 14.sp)
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = NewsGray)
            },
            trailingIcon = {
                if (searchText.isNotBlank()) {
                    IconButton(onClick = {
                        searchText = ""
                        viewModel.searchNews("")
                        keyboardController?.hide()
                    }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear search", tint = NewsGray)
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                if (searchText.isNotBlank()) {
                    viewModel.searchNews(searchText)
                }
                keyboardController?.hide()
            }),
            shape = CircleShape,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = NewsDarkSurface,
                unfocusedContainerColor = NewsDarkSurface,
                focusedTextColor = NewsWhite,
                unfocusedTextColor = NewsWhite,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = NewsWhite
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )

        // Category chips — hidden during active search to avoid confusion
        if (searchText.isBlank()) {
            CategoryChipRow(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { viewModel.selectCategory(it) }
            )
        }

        // Content area
        when (val state = uiState) {
            is NewsUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NewsWhite)
                }
            }

            is NewsUiState.Success -> {
                if (state.articles.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "🔍", fontSize = 48.sp)
                            Text(
                                text = "No articles found",
                                color = NewsGray,
                                fontSize = 16.sp
                            )
                            if (searchText.isNotBlank()) {
                                Text(
                                    text = "Try a different keyword",
                                    color = NewsGray.copy(alpha = 0.6f),
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                } else {
                    val bookmarkedUrls by viewModel.bookmarkedUrls.collectAsState()
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(
                            items = state.articles,
                            key = { it.url }
                        ) { article ->
                            NewsCard(
                                article = article,
                                onClick = { onArticleClick(article) },
                                isBookmarked = article.url in bookmarkedUrls,
                                onBookmarkClick = { viewModel.toggleBookmark(article) }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    }
                }
            }

            is NewsUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(text = "⚠️", fontSize = 48.sp)
                        Text(
                            text = state.message,
                            color = NewsGray,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = { viewModel.retry() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NewsWhite,
                                contentColor = NewsBlack
                            )
                        ) {
                            Text("Retry", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
