package com.example.pertemuan14.ui.component

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pertemuan14.data.model.Article
import com.example.pertemuan14.ui.theme.NewsAccentBlue
import com.example.pertemuan14.ui.theme.NewsBorder
import com.example.pertemuan14.ui.theme.NewsGray
import com.example.pertemuan14.ui.theme.NewsLightGray
import com.example.pertemuan14.ui.theme.NewsWhite
import com.example.pertemuan14.ui.util.formatCommentCount
import com.example.pertemuan14.ui.util.formatTimeAgo
import com.example.pertemuan14.ui.util.formatViewCount

@Composable
fun NewsCard(
    article: Article,
    onClick: () -> Unit,
    isBookmarked: Boolean = false,
    onBookmarkClick: (() -> Unit)? = null,
    onShareClick: (() -> Unit)? = null
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left: text content
            Column(modifier = Modifier.weight(1f)) {
                // Title
                Text(
                    text = article.title,
                    color = NewsWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Source row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2A2A2A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (article.source?.name?.firstOrNull() ?: 'N').toString(),
                            color = NewsWhite,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = article.source?.name ?: "Unknown",
                        color = NewsLightGray,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Stats row: time · views · comments
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = formatTimeAgo(article.publishedAt),
                        color = NewsGray,
                        fontSize = 12.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RemoveRedEye,
                            contentDescription = "Views",
                            tint = NewsGray,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = formatViewCount(article.url),
                            color = NewsGray,
                            fontSize = 12.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = "Comments",
                            tint = NewsGray,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = formatCommentCount(article.url),
                            color = NewsGray,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Right: thumbnail image
            if (!article.urlToImage.isNullOrBlank()) {
                AsyncImage(
                    model = article.urlToImage,
                    contentDescription = article.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1E1E1E)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "📰", fontSize = 28.sp)
                }
            }
        }

        // Bottom action row: share · bookmark · more
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Share
            IconButton(
                onClick = {
                    val action = onShareClick ?: {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "${article.title}\n\n${article.url}")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share article"))
                    }
                    action()
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = NewsGray,
                    modifier = Modifier.size(16.dp)
                )
            }
            // Bookmark
            IconButton(
                onClick = { onBookmarkClick?.invoke() },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = if (isBookmarked) "Remove bookmark" else "Add bookmark",
                    tint = if (isBookmarked) NewsAccentBlue else NewsGray,
                    modifier = Modifier.size(16.dp)
                )
            }
            // More options
            IconButton(
                onClick = {},
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = NewsGray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        HorizontalDivider(
            color = NewsBorder,
            thickness = 0.5.dp
        )
    }
}
