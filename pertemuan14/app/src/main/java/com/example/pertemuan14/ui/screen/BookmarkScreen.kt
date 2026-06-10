package com.example.pertemuan14.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pertemuan14.data.model.Article
import com.example.pertemuan14.ui.component.NewsCard
import com.example.pertemuan14.ui.theme.NewsBlack
import com.example.pertemuan14.ui.theme.NewsGray
import com.example.pertemuan14.ui.theme.NewsWhite
import com.example.pertemuan14.ui.viewmodel.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    viewModel: NewsViewModel,
    onArticleClick: (Article) -> Unit
) {
    val bookmarkedArticles by viewModel.bookmarkedArticles.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NewsBlack)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Bookmarks",
                    color = NewsWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = NewsBlack
            )
        )

        if (bookmarkedArticles.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(horizontal = 40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        tint = NewsGray,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "No bookmarks yet",
                        color = NewsGray,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Tap the 🔖 icon on any article\nto save it here",
                        color = NewsGray.copy(alpha = 0.6f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    items = bookmarkedArticles,
                    key = { it.url }
                ) { article ->
                    NewsCard(
                        article = article,
                        onClick = { onArticleClick(article) },
                        isBookmarked = true,
                        onBookmarkClick = { viewModel.toggleBookmark(article) }
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}
