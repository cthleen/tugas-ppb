package com.example.pertemuan13.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class FeatureItem(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val iconBg: Color
)

@Composable
fun IntroScreen(onGetStarted: () -> Unit) {

    val features = listOf(
        FeatureItem(
            icon = Icons.Default.PersonAdd,
            title = "Tambah Siswa",
            description = "Daftarkan siswa baru dengan nama, email, dan nomor HP",
            iconBg = Color(0xFF1976D2)
        ),
        FeatureItem(
            icon = Icons.Default.Search,
            title = "Pencarian Real-time",
            description = "Cari siswa dengan filter berdasarkan nama, email, atau nomor HP",
            iconBg = Color(0xFF388E3C)
        ),
        FeatureItem(
            icon = Icons.Default.Edit,
            title = "Manajemen Data",
            description = "Perbarui atau hapus data siswa dengan mudah dan cepat",
            iconBg = Color(0xFFFBC02D)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE3F2FD),
                        Color.White
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // App Logo/Icon Placeholder
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFF1976D2).copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color(0xFF1976D2)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Title Section
        Text(
            text = "SiswaHub",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1976D2)
        )

        Text(
            text = "Sistem Manajemen Data Siswa",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Features List
        features.forEach { feature ->
            FeatureRow(feature)
            Spacer(modifier = Modifier.height(24.dp))
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(32.dp))

        // Get started button
        Button(
            onClick = onGetStarted,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1976D2)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Mulai Sekarang",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun FeatureRow(feature: FeatureItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(feature.iconBg.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = feature.icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = feature.iconBg
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = feature.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )
            Text(
                text = feature.description,
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp
            )
        }
    }
}
