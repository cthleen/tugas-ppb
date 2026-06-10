package com.example.pertemuan14.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pertemuan14.data.model.Article
import com.example.pertemuan14.ui.theme.NewsAccentBlue
import com.example.pertemuan14.ui.theme.NewsBlack
import com.example.pertemuan14.ui.theme.NewsBorder
import com.example.pertemuan14.ui.theme.NewsGray
import com.example.pertemuan14.ui.theme.NewsLightGray
import com.example.pertemuan14.ui.theme.NewsWhite
import com.example.pertemuan14.ui.util.formatTimeAgo
import com.example.pertemuan14.ui.viewmodel.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    article: Article,
    viewModel: NewsViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val bookmarkedUrls by viewModel.bookmarkedUrls.collectAsState()
    val isBookmarked = article.url in bookmarkedUrls

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NewsBlack)
            .verticalScroll(rememberScrollState())
    ) {
        // Top App Bar
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = NewsWhite
                    )
                }
            },
            title = {},
            actions = {
                // Bookmark toggle
                IconButton(onClick = { viewModel.toggleBookmark(article) }) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = if (isBookmarked) "Remove bookmark" else "Add bookmark",
                        tint = if (isBookmarked) NewsAccentBlue else NewsWhite
                    )
                }
                // Share
                IconButton(onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "${article.title}\n\n${article.url}")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share article"))
                }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = NewsWhite
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = NewsBlack
            )
        )

        // Hero image
        if (!article.urlToImage.isNullOrBlank()) {
            AsyncImage(
                model = article.urlToImage,
                contentDescription = article.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFF1A1A1A)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "📰", fontSize = 64.sp)
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = article.title,
                color = NewsWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Source row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2A2A2A)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (article.source?.name?.firstOrNull() ?: 'N').toString(),
                        color = NewsWhite,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = article.source?.name ?: "Unknown Source",
                    color = NewsLightGray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(text = "·", color = NewsGray, fontSize = 14.sp)
                Text(
                    text = "Following",
                    color = NewsAccentBlue,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Time & author
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatTimeAgo(article.publishedAt),
                    color = NewsGray,
                    fontSize = 12.sp
                )
                if (!article.author.isNullOrBlank()) {
                    Text(text = "·", color = NewsGray, fontSize = 12.sp)
                    Text(
                        text = article.author,
                        color = NewsGray,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(color = NewsBorder, thickness = 0.5.dp)

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            if (!article.description.isNullOrBlank()) {
                Text(
                    text = article.description,
                    color = NewsLightGray,
                    fontSize = 16.sp,
                    lineHeight = 26.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Content — strip "[+N chars]" truncation marker added by NewsAPI
            val cleanContent = article.content
                ?.replace(Regex("""\[\+\d+ chars\]"""), "")
                ?.trim()
                ?.takeIf { it.isNotBlank() }

            if (cleanContent != null) {
                Text(
                    text = cleanContent,
                    color = NewsGray,
                    fontSize = 15.sp,
                    lineHeight = 25.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // --- Read Full Article Button ---
            Button(
                onClick = {
                    runCatching {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                        context.startActivity(intent)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NewsWhite,
                    contentColor = NewsBlack
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Launch,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Read Full Article",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
