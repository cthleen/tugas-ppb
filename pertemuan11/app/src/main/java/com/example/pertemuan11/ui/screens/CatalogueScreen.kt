package com.example.pertemuan11.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pertemuan11.model.Product
import com.example.pertemuan11.ui.components.ProductGridItem
import com.example.pertemuan11.ui.theme.*

@Composable
fun CatalogueScreen(products: List<Product>, onProductClick: (Product) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredProducts = remember(searchQuery, products) {
        products.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.category.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {

        Spacer(Modifier.height(8.dp))

        Text(
            "Semua Barang",
            style      = MaterialTheme.typography.headlineMedium,
            color      = MarketTextDark,
            fontWeight = FontWeight.ExtraBold
        )

        Text(
            "${filteredProducts.size} barang tersedia",
            style = MaterialTheme.typography.bodySmall,
            color = MarketTextGrey
        )

        Spacer(Modifier.height(14.dp))

        OutlinedTextField(
            value         = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder   = {
                Text(
                    "Cari barang di katalog...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MarketTextLight
                )
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = MarketTextLight)
            },
            modifier   = Modifier.fillMaxWidth(),
            shape      = RoundedCornerShape(14.dp),
            singleLine = true,
            colors     = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MarketSurface2,
                focusedContainerColor   = MarketSurface2,
                unfocusedBorderColor    = Color.Transparent,
                focusedBorderColor      = MarketTeal
            )
        )

        Spacer(Modifier.height(16.dp))

        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint     = MarketTextLight,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Barang tidak ditemukan",
                        style = MaterialTheme.typography.titleSmall,
                        color = MarketTextGrey
                    )
                    Text(
                        "Coba kata kunci lain",
                        style = MaterialTheme.typography.bodySmall,
                        color = MarketTextLight
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns                = GridCells.Fixed(2),
                horizontalArrangement  = Arrangement.spacedBy(12.dp),
                verticalArrangement    = Arrangement.spacedBy(12.dp),
                contentPadding         = PaddingValues(bottom = 24.dp),
                modifier               = Modifier.fillMaxSize()
            ) {
                items(filteredProducts) { product ->
                    ProductGridItem(product = product, onClick = { onProductClick(product) })
                }
            }
        }
    }
}