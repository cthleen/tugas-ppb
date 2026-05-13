package com.example.pertemuan11.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pertemuan11.model.Product
import com.example.pertemuan11.ui.theme.*

@Composable
fun ProductDetailScreen(product: Product, onBack: () -> Unit) {
    var activeTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Tentang Barang", "Ulasan", "Penjual")

    Scaffold(
        containerColor = MarketBackground,
        bottomBar = {
            Surface(
                modifier       = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color          = MarketSurface
            ) {
                Row(
                    modifier              = Modifier
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Total Harga",
                            style = MaterialTheme.typography.labelMedium,
                            color = MarketTextGrey
                        )
                        Text(
                            "Rp ${product.price}",
                            style      = MaterialTheme.typography.headlineMedium,
                            color      = MarketTeal,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Button(
                        onClick  = {},
                        modifier = Modifier.height(50.dp).width(160.dp),
                        shape    = RoundedCornerShape(14.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = MarketTeal)
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Beli Sekarang",
                            style      = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // ── Top Bar ────────────────────────────────────────────
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                TopBarIconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", modifier = Modifier.size(20.dp))
                }
                Text(
                    "Detail Produk",
                    style = MaterialTheme.typography.titleMedium,
                    color = MarketTextDark
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TopBarIconButton(onClick = {}) {
                        Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Favorit", modifier = Modifier.size(20.dp))
                    }
                    TopBarIconButton(onClick = {}) {
                        Icon(Icons.Outlined.Share, contentDescription = "Bagikan", modifier = Modifier.size(20.dp))
                    }
                    TopBarIconButton(onClick = {}) {
                        Icon(Icons.Outlined.ShoppingCart, contentDescription = "Keranjang", modifier = Modifier.size(20.dp))
                    }
                }
            }

            // ── Product Image ──────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(260.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MarketSurface2),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Inventory2,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint     = MarketTeal.copy(alpha = 0.4f)
                )
            }

            Spacer(Modifier.height(16.dp))

            // ── Info Card ──────────────────────────────────────────
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape    = RoundedCornerShape(20.dp),
                color    = MarketSurface
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    // Category tag
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MarketTealLight
                    ) {
                        Text(
                            product.category,
                            style    = MaterialTheme.typography.labelSmall,
                            color    = MarketTeal,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        product.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MarketTextDark
                    )

                    Spacer(Modifier.height(10.dp))

                    // Rating row
                    Row(
                        verticalAlignment  = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint     = Color(0xFFF59E0B),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            "${product.rating}",
                            style      = MaterialTheme.typography.labelMedium,
                            color      = MarketTextDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "·  ${product.reviews} ulasan  ·  ${product.soldCount} terjual",
                            style = MaterialTheme.typography.bodySmall,
                            color = MarketTextGrey
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    // Tabs
                    TabRow(
                        selectedTabIndex = activeTab,
                        containerColor   = Color.Transparent,
                        contentColor     = MarketTeal,
                        divider          = { HorizontalDivider(color = MarketBorder) }
                    ) {
                        tabs.forEachIndexed { idx, title ->
                            Tab(
                                selected         = activeTab == idx,
                                onClick          = { activeTab = idx },
                                selectedContentColor   = MarketTeal,
                                unselectedContentColor = MarketTextGrey,
                                text = {
                                    Text(
                                        title,
                                        style      = MaterialTheme.typography.labelMedium,
                                        fontWeight = if (activeTab == idx) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    when (activeTab) {
                        0 -> {
                            // Spesifikasi chips
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                SpecChip(label = "Brand", value = product.brand)
                                SpecChip(label = "Warna", value = product.color)
                            }
                            Spacer(Modifier.height(14.dp))
                            Text(
                                product.description,
                                style      = MaterialTheme.typography.bodyMedium,
                                color      = MarketTextGrey,
                                lineHeight = 22.sp
                            )
                        }
                        1 -> {
                            Text(
                                "Belum ada ulasan untuk produk ini.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MarketTextGrey
                            )
                        }
                        2 -> {
                            Text(
                                "Informasi penjual akan ditampilkan di sini.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MarketTextGrey
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TopBarIconButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.size(38.dp),
        shape    = RoundedCornerShape(11.dp),
        color    = MarketSurface2,
        onClick  = onClick
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}

@Composable
private fun SpecChip(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MarketSurface2
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                "$label: ",
                style = MaterialTheme.typography.bodySmall,
                color = MarketTextGrey
            )
            Text(
                value,
                style      = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color      = MarketTextDark
            )
        }
    }
}