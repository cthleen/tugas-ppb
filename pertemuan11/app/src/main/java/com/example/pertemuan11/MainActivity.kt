package com.example.pertemuan11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pertemuan11.model.Product
import com.example.pertemuan11.ui.screens.*
import com.example.pertemuan11.ui.theme.MarketBackground
import com.example.pertemuan11.ui.theme.MarketSiswaTheme
import com.example.pertemuan11.ui.theme.MarketTeal
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarketSiswaTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var currentScreen by remember { mutableStateOf("home") }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    val productList = remember { mutableStateListOf<Product>() }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Inisialisasi 15 Data Dummy Premium
    LaunchedEffect(Unit) {
        if (productList.isEmpty()) {
            val dummies = listOf(
                Product(name = "Buku Kalkulus Purcell", price = "75.000", category = "Buku", description = "Kondisi 90% mulus, sangat berguna untuk TPB."),
                Product(name = "Jaket Almamater", price = "120.000", category = "Pakaian", description = "Ukuran L, bahan premium, baru dipakai sekali."),
                Product(name = "Buku Sistem Operasi", price = "50.000", category = "Buku", description = "Edisi terbaru, materi kernel dan thread lengkap."),
                Product(name = "Kemeja Sidang Putih", price = "45.000", category = "Pakaian", description = "Bahan katun adem, pas untuk sidang/magang."),
                Product(name = "Sepatu Pantofel", price = "150.000", category = "Sepatu", description = "Ukuran 42, kulit sintetis berkualitas."),
                Product(name = "Laptop Stand", price = "85.000", category = "Aksesoris", description = "Alumunium, kokoh, bisa dilipat."),
                Product(name = "Tumbler Stainless", price = "60.000", category = "Lainnya", description = "Tahan panas/dingin hingga 12 jam."),
                Product(name = "Keyboard RGB", price = "250.000", category = "Elektronik", description = "Mechanical blue switch, suara mantap."),
                Product(name = "Mouse Wireless", price = "95.000", category = "Elektronik", description = "Silent click, baterai awet 6 bulan."),
                Product(name = "Tas Ransel Kuliah", price = "110.000", category = "Tas", description = "Muat laptop 15 inch, banyak sekat."),
                Product(name = "Lampu Belajar LED", price = "35.000", category = "Elektronik", description = "Bisa di-charge, 3 mode warna."),
                Product(name = "Kalkulator Scientific", price = "180.000", category = "Alat Tulis", description = "Sangat diperlukan untuk Teknik/MIPA."),
                Product(name = "Jas Lab Kimia", price = "55.000", category = "Pakaian", description = "Ukuran M, bersih tanpa noda kimia."),
                Product(name = "Set Penggaris Teknik", price = "25.000", category = "Alat Tulis", description = "Lengkap dengan busur dan jangka."),
                Product(name = "Flashdisk 64GB", price = "70.000", category = "Elektronik", description = "USB 3.0 ultra speed, ori Sandisk.")
            )
            productList.addAll(dummies)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            // Sembunyikan Top Bar jika di halaman Detail agar sesuai referensi
            if (selectedProduct == null) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "MarketSiswa",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        if (currentScreen != "home") {
                            IconButton(onClick = { currentScreen = "home" }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MarketBackground
                    )
                )
            }
        },
        bottomBar = {
            if (selectedProduct == null) {
                NavigationBar(
                    containerColor = MarketBackground,
                    tonalElevation = 8.dp
                ) {
                    val navItems = listOf(
                        Triple("home", Icons.Default.Home, "Beranda"),
                        Triple("catalogue", Icons.Default.Search, "Katalog"),
                        Triple("profile", Icons.Default.Person, "Profil")
                    )
                    navItems.forEach { (screen, icon, label) ->
                        NavigationBarItem(
                            selected = currentScreen == screen,
                            onClick = { currentScreen = screen },
                            label = { Text(label, style = MaterialTheme.typography.labelMedium) },
                            icon = { Icon(icon, contentDescription = null) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MarketTeal,
                                selectedTextColor = MarketTeal,
                                indicatorColor = MarketTeal.copy(alpha = 0.1f)
                            )
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (selectedProduct == null && (currentScreen == "home" || currentScreen == "catalogue")) {
                ExtendedFloatingActionButton(
                    onClick = { currentScreen = "add" },
                    containerColor = MarketTeal,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    elevation = FloatingActionButtonDefaults.elevation(4.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Jual Barang", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(if (selectedProduct == null) innerPadding else PaddingValues(0.dp))
                .fillMaxSize()
                .background(MarketBackground)
        ) {
            AnimatedContent(
                targetState = if (selectedProduct != null) "detail" else currentScreen,
                label = "ScreenTransition",
                transitionSpec = { fadeIn() togetherWith fadeOut() }
            ) { state ->
                when (state) {
                    "home" -> HomeScreen(productList) { selectedProduct = it }
                    "catalogue" -> CatalogueScreen(productList) { selectedProduct = it }
                    "detail" -> selectedProduct?.let {
                        ProductDetailScreen(it) { selectedProduct = null }
                    }
                    "add" -> AddProductScreen(
                        onProductAdded = { newProduct ->
                            productList.add(0, newProduct)
                            scope.launch {
                                currentScreen = "home"
                                snackbarHostState.showSnackbar("Barang berhasil dipublikasikan!")
                            }
                        }
                    )
                    "profile" -> ProfileScreen()
                }
            }
        }
    }
}
