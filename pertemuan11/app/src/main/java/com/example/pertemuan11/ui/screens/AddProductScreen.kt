package com.example.pertemuan11.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pertemuan11.model.Product
import com.example.pertemuan11.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private data class CategoryOption(val label: String, val icon: ImageVector)

private val categoryOptions = listOf(
    CategoryOption("Buku",       Icons.Outlined.MenuBook),
    CategoryOption("Pakaian",    Icons.Outlined.Checkroom),
    CategoryOption("Elektronik", Icons.Outlined.Devices),
    CategoryOption("Alat Tulis", Icons.Outlined.Edit),
    CategoryOption("Tas",        Icons.Outlined.Work),
    CategoryOption("Lainnya",    Icons.Outlined.GridView),
)

@Composable
fun AddProductScreen(onProductAdded: (Product) -> Unit) {
    var name            by remember { mutableStateOf("") }
    var price           by remember { mutableStateOf("") }
    var desc            by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Umum") }
    var isLoading       by remember { mutableStateOf(false) }
    val scope           = rememberCoroutineScope()
    val scrollState     = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MarketBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        // ── Title ──────────────────────────────────────────────────
        Column {
            Text(
                "Jual Barang",
                style      = MaterialTheme.typography.headlineMedium,
                color      = MarketTextDark,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                "Isi detail barang yang ingin dijual",
                style = MaterialTheme.typography.bodySmall,
                color = MarketTextGrey
            )
        }

        // ── Upload Photo Box ───────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MarketSurface)
                .border(
                    BorderStroke(1.5.dp, MarketBorder),
                    RoundedCornerShape(16.dp)
                )
                .clickable {},
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    modifier = Modifier.size(44.dp),
                    shape    = RoundedCornerShape(12.dp),
                    color    = MarketTealLight
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Outlined.CameraAlt,
                            contentDescription = null,
                            tint     = MarketTeal,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "Upload Foto Barang",
                    style      = MaterialTheme.typography.labelMedium,
                    color      = MarketTextDark,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Maks. 5 foto  ·  JPG, PNG",
                    style = MaterialTheme.typography.bodySmall,
                    color = MarketTextLight
                )
            }
        }

        // ── Form Card ──────────────────────────────────────────────
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MarketSurface
        ) {
            Column(
                modifier            = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FormSectionLabel("Informasi Barang")

                // Nama
                AddFormField(label = "Nama Barang") {
                    OutlinedTextField(
                        value         = name,
                        onValueChange = { name = it },
                        placeholder   = { Text("Contoh: Buku Kalkulus Purcell", style = MaterialTheme.typography.bodyMedium) },
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(12.dp),
                        singleLine    = true,
                        colors        = addFieldColors()
                    )
                }

                // Harga
                AddFormField(label = "Harga (Rp)") {
                    OutlinedTextField(
                        value         = price,
                        onValueChange = { price = it },
                        placeholder   = { Text("Contoh: 75000", style = MaterialTheme.typography.bodyMedium) },
                        leadingIcon   = {
                            Text(
                                "Rp",
                                style      = MaterialTheme.typography.labelMedium,
                                color      = MarketTextGrey,
                                fontWeight = FontWeight.Bold,
                                modifier   = Modifier.padding(start = 4.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier        = Modifier.fillMaxWidth(),
                        shape           = RoundedCornerShape(12.dp),
                        singleLine      = true,
                        colors          = addFieldColors()
                    )
                }

                // Deskripsi
                AddFormField(label = "Deskripsi Singkat") {
                    OutlinedTextField(
                        value         = desc,
                        onValueChange = { desc = it },
                        placeholder   = { Text("Kondisi, keunggulan, atau detail lain...", style = MaterialTheme.typography.bodyMedium) },
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(12.dp),
                        minLines      = 3,
                        maxLines      = 5,
                        colors        = addFieldColors()
                    )
                }
            }
        }

        // ── Kategori ───────────────────────────────────────────────
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MarketSurface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                FormSectionLabel("Kategori")
                Spacer(Modifier.height(10.dp))
                // 3-column grid
                val rows = categoryOptions.chunked(3)
                rows.forEach { row ->
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { cat ->
                            val isSelected = selectedCategory == cat.label
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedCategory = cat.label },
                                shape    = RoundedCornerShape(12.dp),
                                color    = if (isSelected) MarketTealLight else MarketSurface2,
                                border   = if (isSelected)
                                    BorderStroke(1.5.dp, MarketTeal) else
                                    BorderStroke(1.dp, MarketBorder)
                            ) {
                                Column(
                                    modifier                = Modifier.padding(vertical = 10.dp),
                                    horizontalAlignment     = Alignment.CenterHorizontally,
                                    verticalArrangement     = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector        = cat.icon,
                                        contentDescription = null,
                                        tint               = if (isSelected) MarketTeal else MarketTextGrey,
                                        modifier           = Modifier.size(20.dp)
                                    )
                                    Text(
                                        cat.label,
                                        style      = MaterialTheme.typography.labelSmall,
                                        color      = if (isSelected) MarketTeal else MarketTextGrey,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                        // pad empty cells
                        repeat(3 - row.size) { Box(Modifier.weight(1f)) }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }

        // ── Tombol Publikasi ───────────────────────────────────────
        Button(
            onClick = {
                isLoading = true
                scope.launch {
                    delay(1200)
                    onProductAdded(
                        Product(
                            name        = name.ifBlank { "Produk Baru" },
                            price       = price.ifBlank { "0" },
                            category    = selectedCategory,
                            description = desc
                        )
                    )
                    isLoading = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            enabled = name.isNotBlank() && price.isNotBlank() && !isLoading,
            shape   = RoundedCornerShape(14.dp),
            colors  = ButtonDefaults.buttonColors(containerColor = MarketTeal)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier    = Modifier.size(22.dp),
                    color       = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    Icons.Outlined.Publish,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Publikasikan Sekarang",
                    style      = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color      = Color.White
                )
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun FormSectionLabel(text: String) {
    Text(
        text       = text,
        style      = MaterialTheme.typography.titleSmall,
        color      = MarketTextDark,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun AddFormField(label: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MarketTextGrey
        )
        content()
    }
}

@Composable
private fun addFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor      = MarketTeal,
    unfocusedBorderColor    = MarketBorder,
    focusedLabelColor       = MarketTeal,
    unfocusedContainerColor = MarketSurface2,
    focusedContainerColor   = MarketSurface
)