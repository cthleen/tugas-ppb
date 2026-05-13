package com.example.pertemuan11.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
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
import com.example.pertemuan11.ui.theme.*

private data class MenuItem(val label: String, val icon: ImageVector, val badge: String? = null)

private val mainMenuItems = listOf(
    MenuItem("Barang Saya",     Icons.Outlined.Inventory2),
    MenuItem("Pesan & Chat",    Icons.Outlined.Chat,    badge = "2"),
    MenuItem("Daftar Favorit",  Icons.Outlined.FavoriteBorder),
    MenuItem("Ulasan Saya",     Icons.Outlined.StarBorder),
)

private val otherMenuItems = listOf(
    MenuItem("Pengaturan",          Icons.Outlined.Settings),
    MenuItem("Privasi & Keamanan",  Icons.Outlined.Shield),
    MenuItem("Bantuan",             Icons.Outlined.HelpOutline),
)

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MarketBackground)
            .verticalScroll(rememberScrollState())
    ) {

        // ── Hero Header ────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(MarketTeal, Color(0xFF1AAD96))))
                .padding(top = 28.dp, bottom = 56.dp, start = 20.dp, end = 20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                // Avatar with edit overlay
                Box(contentAlignment = Alignment.BottomEnd) {
                    Surface(
                        modifier = Modifier.size(80.dp),
                        shape    = RoundedCornerShape(22.dp),
                        color    = Color.White.copy(alpha = 0.20f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Outlined.Person,
                                contentDescription = null,
                                tint     = Color.White,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                    }
                    Surface(
                        modifier = Modifier
                            .size(26.dp)
                            .offset(x = 4.dp, y = 4.dp),
                        shape  = CircleShape,
                        color  = MarketAccent
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Outlined.Edit,
                                contentDescription = "Edit profil",
                                tint     = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    "John Siswa",
                    style      = MaterialTheme.typography.titleLarge,
                    color      = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Teknik Informatika  ·  Semester 5",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.80f)
                )
            }
        }

        // ── Stats Card (overlap hero) ──────────────────────────────
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-28).dp)
                .padding(horizontal = 20.dp),
            shape    = RoundedCornerShape(16.dp),
            color    = MarketSurface,
            shadowElevation = 4.dp
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                StatItem("12",  "Dijual",   Modifier.weight(1f))
                VerticalDivider(modifier = Modifier.height(50.dp).align(Alignment.CenterVertically))
                StatItem("8",   "Terjual",  Modifier.weight(1f))
                VerticalDivider(modifier = Modifier.height(50.dp).align(Alignment.CenterVertically))
                StatItem("24",  "Favorit",  Modifier.weight(1f))
            }
        }

        // offset spacer to fill the negative offset gap
        Spacer(Modifier.height(0.dp))

        // ── Menu Sections ──────────────────────────────────────────
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {

            MenuGroupLabel("Menu Utama")
            MenuSection(mainMenuItems)

            Spacer(Modifier.height(16.dp))

            MenuGroupLabel("Lainnya")
            MenuSection(otherMenuItems)

            Spacer(Modifier.height(20.dp))

            // Logout button
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(14.dp),
                color    = Color(0xFFFEF2F2)
            ) {
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .clickable {}
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Outlined.Logout,
                        contentDescription = "Keluar",
                        tint     = Color(0xFFEF4444),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Keluar Akun",
                        style      = MaterialTheme.typography.labelLarge,
                        color      = Color(0xFFEF4444),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StatItem(value: String, label: String, modifier: Modifier) {
    Column(
        modifier            = modifier.padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            value,
            style      = MaterialTheme.typography.headlineSmall,
            color      = MarketTextDark,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MarketTextGrey
        )
    }
}

@Composable
private fun MenuGroupLabel(text: String) {
    Text(
        text       = text,
        style      = MaterialTheme.typography.labelMedium,
        color      = MarketTextLight,
        modifier   = Modifier.padding(vertical = 10.dp)
    )
}

@Composable
private fun MenuSection(items: List<MenuItem>) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MarketSurface
    ) {
        Column {
            items.forEachIndexed { idx, item ->
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .clickable {}
                        .padding(horizontal = 16.dp, vertical = 13.dp),
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(36.dp),
                        shape    = RoundedCornerShape(10.dp),
                        color    = MarketTealLight
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                item.icon,
                                contentDescription = null,
                                tint     = MarketTeal,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(
                        item.label,
                        style    = MaterialTheme.typography.titleSmall,
                        color    = MarketTextDark,
                        modifier = Modifier.weight(1f)
                    )
                    if (item.badge != null) {
                        Surface(
                            shape = CircleShape,
                            color = MarketAccent
                        ) {
                            Text(
                                item.badge,
                                style    = MaterialTheme.typography.labelSmall,
                                color    = Color.White,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(Modifier.width(6.dp))
                    }
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint     = MarketTextLight,
                        modifier = Modifier.size(18.dp)
                    )
                }
                if (idx < items.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 64.dp),
                        color    = MarketBorder
                    )
                }
            }
        }
    }
}