package com.example.pertemuan11.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pertemuan11.model.Product
import com.example.pertemuan11.ui.components.ProductGridItem
import com.example.pertemuan11.ui.theme.*

private data class Category(val label: String, val icon: ImageVector)

private val categories = listOf(
    Category("Buku",       Icons.Outlined.MenuBook),
    Category("Pakaian",    Icons.Outlined.Checkroom),
    Category("Elektronik", Icons.Outlined.Devices),
    Category("Alat Tulis", Icons.Outlined.Edit),
    Category("Lainnya",    Icons.Outlined.GridView),
)

@Composable
fun HomeScreen(products: List<Product>, onProductClick: (Product) -> Unit) {
    LazyColumn(
        modifier             = Modifier.fillMaxSize(),
        contentPadding       = PaddingValues(bottom = 24.dp),
        verticalArrangement  = Arrangement.spacedBy(0.dp)
    ) {

        // ── Header ─────────────────────────────────────────────────
        item {
            Surface(color = MarketSurface, shadowElevation = 0.dp) {
                Column {
                    Row(
                        modifier              = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Selamat pagi, John",
                                style = MaterialTheme.typography.bodySmall,
                                color = MarketTextGrey
                            )
                            Text(
                                "MarketSiswa",
                                style      = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color      = MarketTextDark,
                                letterSpacing = (-0.3).sp
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            HeaderIconButton(icon = Icons.Outlined.NotificationsNone, badge = "3")
                            HeaderIconButton(icon = Icons.Outlined.ShoppingCart,      badge = "1")
                        }
                    }

                    // Search bar
                    OutlinedTextField(
                        value         = "",
                        onValueChange = {},
                        placeholder   = {
                            Text(
                                "Cari buku, elektronik, pakaian...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MarketTextLight
                            )
                        },
                        leadingIcon   = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = MarketTextLight)
                        },
                        modifier      = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 14.dp),
                        shape         = RoundedCornerShape(14.dp),
                        singleLine    = true,
                        colors        = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MarketSurface2,
                            focusedContainerColor   = MarketSurface2,
                            unfocusedBorderColor    = Color.Transparent,
                            focusedBorderColor      = MarketTeal
                        )
                    )
                }
            }
        }

        // ── Banner ─────────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(listOf(MarketTeal, Color(0xFF1AAD96)))
                    )
                    .height(140.dp)
            ) {
                // decorative circles
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .offset(x = 220.dp, y = (-30).dp)
                        .background(Color.White.copy(alpha = 0.07f), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .offset(x = 250.dp, y = 60.dp)
                        .background(Color.White.copy(alpha = 0.05f), CircleShape)
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(20.dp)
                ) {
                    Text(
                        "KAMPUS DEAL",
                        style         = MaterialTheme.typography.labelSmall,
                        color         = Color.White.copy(alpha = 0.75f),
                        letterSpacing = 1.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Hemat Pangkal\nKaya",
                        style      = MaterialTheme.typography.headlineMedium,
                        color      = Color.White,
                        lineHeight = 26.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Buku & alat kuliah berkualitas",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.80f)
                    )
                    Spacer(Modifier.height(12.dp))
                    Surface(
                        shape  = RoundedCornerShape(8.dp),
                        color  = Color.White,
                        onClick = {}
                    ) {
                        Row(
                            modifier          = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "Cek Sekarang",
                                style      = MaterialTheme.typography.labelMedium,
                                color      = MarketTealDark,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint     = MarketTealDark,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }
        }

        // ── Kategori ───────────────────────────────────────────────
        item {
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        "Kategori",
                        style = MaterialTheme.typography.titleMedium,
                        color = MarketTextDark
                    )
                    Text(
                        "Lihat semua",
                        style    = MaterialTheme.typography.labelMedium,
                        color    = MarketTeal,
                        modifier = Modifier.clickable {}
                    )
                }
                LazyRow(
                    contentPadding        = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categories) { cat ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier            = Modifier.clickable {}
                        ) {
                            Box(
                                modifier        = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MarketTealLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector        = cat.icon,
                                    contentDescription = null,
                                    tint               = MarketTeal,
                                    modifier           = Modifier.size(24.dp)
                                )
                            }
                            Spacer(Modifier.height(6.dp))
                            Text(
                                cat.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = MarketTextGrey
                            )
                        }
                    }
                }
            }
        }

        // ── Produk Populer header ──────────────────────────────────
        item {
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    "Produk Populer",
                    style = MaterialTheme.typography.titleMedium,
                    color = MarketTextDark
                )
                Text(
                    "Lihat semua",
                    style    = MaterialTheme.typography.labelMedium,
                    color    = MarketTeal,
                    modifier = Modifier.clickable {}
                )
            }
        }

        // ── Grid Produk ────────────────────────────────────────────
        val chunked = products.chunked(2)
        items(chunked) { pair ->
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProductGridItem(
                    product  = pair[0],
                    onClick  = { onProductClick(pair[0]) },
                    modifier = Modifier.weight(1f)
                )
                if (pair.size > 1) {
                    ProductGridItem(
                        product  = pair[1],
                        onClick  = { onProductClick(pair[1]) },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Box(Modifier.weight(1f))
                }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun HeaderIconButton(icon: ImageVector, badge: String? = null) {
    Box {
        Surface(
            modifier = Modifier.size(38.dp),
            shape    = RoundedCornerShape(11.dp),
            color    = MarketSurface2,
            onClick  = {}
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = MarketTextDark, modifier = Modifier.size(20.dp))
            }
        }
        if (badge != null) {
            Surface(
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 3.dp, y = (-3).dp),
                shape = CircleShape,
                color = MarketAccent
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(badge, style = MaterialTheme.typography.labelSmall, color = Color.White, fontSize = 8.sp)
                }
            }
        }
    }
}