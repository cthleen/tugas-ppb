package com.example.pertemuan11.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pertemuan11.ui.theme.*

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        // ── Hero Header ────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(listOf(MarketTeal, Color(0xFF1AAD96)))
                )
                .padding(top = 56.dp, start = 28.dp, end = 28.dp, bottom = 40.dp)
        ) {
            Column {
                // App icon placeholder
                Surface(
                    modifier  = Modifier.size(60.dp),
                    shape     = RoundedCornerShape(18.dp),
                    color     = Color.White.copy(alpha = 0.18f),
                    tonalElevation = 0.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = null,
                            tint   = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
                Text(
                    text       = "Halo, Selamat Datang!",
                    style      = MaterialTheme.typography.displayMedium,
                    color      = Color.White
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text  = "Login untuk mulai belanja dan jual barang",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.80f)
                )
            }
        }

        // ── Form Body ──────────────────────────────────────────────
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape    = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color    = MarketSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 28.dp),
            ) {

                // Email
                FieldLabel("Email")
                OutlinedTextField(
                    value         = email,
                    onValueChange = { email = it },
                    placeholder   = { Text("john@student.ac.id", style = MaterialTheme.typography.bodyMedium) },
                    leadingIcon   = { Icon(Icons.Default.Email, contentDescription = null, tint = MarketTextLight) },
                    modifier      = Modifier.fillMaxWidth(),
                    shape         = RoundedCornerShape(12.dp),
                    singleLine    = true,
                    colors        = fieldColors()
                )

                Spacer(Modifier.height(14.dp))

                // Password
                FieldLabel("Password")
                OutlinedTextField(
                    value               = password,
                    onValueChange       = { password = it },
                    placeholder         = { Text("Masukkan password", style = MaterialTheme.typography.bodyMedium) },
                    leadingIcon         = { Icon(Icons.Default.Lock, contentDescription = null, tint = MarketTextLight) },
                    trailingIcon        = {
                        IconButton(onClick = { showPass = !showPass }) {
                            Icon(
                                imageVector        = if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (showPass) "Sembunyikan" else "Tampilkan",
                                tint               = MarketTextLight
                            )
                        }
                    },
                    visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier             = Modifier.fillMaxWidth(),
                    shape                = RoundedCornerShape(12.dp),
                    singleLine           = true,
                    colors               = fieldColors()
                )

                // Lupa password
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = {}) {
                        Text(
                            "Lupa password?",
                            style = MaterialTheme.typography.labelMedium,
                            color = MarketTeal
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                // ── Tombol Login — langsung masuk tanpa validasi ───
                Button(
                    onClick  = onLoginSuccess,           // bypass langsung
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape  = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MarketTeal)
                ) {
                    Text(
                        "Masuk Sekarang",
                        style      = MaterialTheme.typography.labelLarge,
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color.White
                    )
                }

                // Divider
                Spacer(Modifier.height(24.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = MarketBorder)
                    Text(
                        "  atau lanjut dengan  ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MarketTextLight
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = MarketBorder)
                }
                Spacer(Modifier.height(16.dp))

                // Social login
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    SocialButton("G")
                    Spacer(Modifier.width(12.dp))
                    SocialButton("f")
                    Spacer(Modifier.width(12.dp))
                    SocialButton("in")
                }

                Spacer(Modifier.weight(1f))

                // Daftar
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        "Belum punya akun? ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MarketTextGrey
                    )
                    TextButton(onClick = {}, contentPadding = PaddingValues(0.dp)) {
                        Text(
                            "Daftar sekarang",
                            style      = MaterialTheme.typography.labelMedium,
                            color      = MarketTeal,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text     = text,
        style    = MaterialTheme.typography.labelMedium,
        color    = MarketTextDark,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor   = MarketTeal,
    unfocusedBorderColor = MarketBorder,
    focusedLabelColor    = MarketTeal,
    unfocusedContainerColor = MarketSurface2,
    focusedContainerColor   = MarketSurface
)

@Composable
private fun SocialButton(label: String) {
    Surface(
        modifier      = Modifier.size(width = 72.dp, height = 44.dp),
        shape         = RoundedCornerShape(12.dp),
        color         = MarketSurface2,
        tonalElevation = 0.dp,
        onClick       = {}
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text       = label,
                style      = MaterialTheme.typography.titleSmall,
                color      = MarketTextDark,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}