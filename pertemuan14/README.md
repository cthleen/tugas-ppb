# 📰 NewsApp — Android Modern News Application

Aplikasi Android modern untuk menampilkan berita terkini dari **NewsAPI.org** menggunakan arsitektur **MVVM**, **Jetpack Compose**, **Retrofit**, **Kotlin Coroutines**, dan **StateFlow**.

---

## 🧱 Arsitektur Project

```
MVVM (Model - View - ViewModel)
│
├── Data Layer
│   ├── Model          → Article, NewsResponse
│   ├── Remote         → ApiService (Retrofit), RetrofitClient
│   └── Repository     → NewsRepository
│
├── UI Layer
│   ├── State          → NewsUiState (sealed class)
│   ├── ViewModel      → NewsViewModel
│   ├── Screen         → HomeScreen, DetailScreen, BookmarkScreen
│   ├── Component      → NewsCard, CategoryChip
│   ├── Navigation     → NavGraph (Bottom Navigation)
│   └── Theme          → Color, Typography, Theme (Dark Mode)
│
└── MainActivity.kt
```

---

## 📦 Struktur Package

```
com.example.pertemuan14/
├── data/
│   ├── model/
│   │   ├── Article.kt
│   │   └── NewsResponse.kt
│   ├── remote/
│   │   ├── ApiService.kt
│   │   └── RetrofitClient.kt
│   └── repository/
│       └── NewsRepository.kt
├── ui/
│   ├── component/
│   │   ├── NewsCard.kt
│   │   └── CategoryChip.kt
│   ├── navigation/
│   │   └── NavGraph.kt
│   ├── screen/
│   │   ├── HomeScreen.kt
│   │   ├── DetailScreen.kt
│   │   └── BookmarkScreen.kt
│   ├── state/
│   │   └── NewsUiState.kt
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   ├── util/
│   │   └── DateUtils.kt
│   └── viewmodel/
│       └── NewsViewModel.kt
└── MainActivity.kt
```

---

## ✨ Fitur Aplikasi

| Fitur | Keterangan |
|---|---|
| 📋 Home Screen | Daftar berita terkini dalam card list |
| 🔍 Search | Pencarian berita dengan keyword |
| 🏷️ Kategori | Filter: All, Technology, Sports, Health, Business, dll. |
| 📄 Detail Screen | Halaman detail artikel lengkap |
| 🌐 Buka di Browser | Tombol "Read Full Article" membuka URL di browser |
| 🔖 Bookmark | Simpan artikel favorit |
| 📌 Halaman Bookmark | Melihat semua artikel yang disimpan |
| ⏳ Loading State | Spinner saat data dimuat |
| ❌ Error State | Pesan error + tombol Retry |
| 🌑 Dark Theme | Desain gelap modern |

---

## 🚀 Langkah-Langkah Pengerjaan

### Langkah 1 — Setup Dependencies

Tambahkan semua library yang diperlukan di `app/build.gradle.kts`:

```kotlin
dependencies {
    // ViewModel + Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.8.9")

    // Retrofit (HTTP Client)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // OkHttp Logging
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Coil (Image Loading)
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Material Icons Extended
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
}
```

> **Catatan:** Pada AGP 9.x, blok `kotlinOptions { jvmTarget = "11" }` sudah tidak didukung dan harus dihapus. Plugin Kotlin Compose menangani JVM target secara otomatis.

---

### Langkah 2 — Tambah Internet Permission

Di `AndroidManifest.xml`, tambahkan permission internet **sebelum** tag `<application>`:

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

---

### Langkah 3 — Buat Data Model

**`Article.kt`** — Model satu artikel berita:

```kotlin
data class Article(
    @SerializedName("source")      val source: Source?,
    @SerializedName("author")      val author: String?,
    @SerializedName("title")       val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("url")         val url: String,
    @SerializedName("urlToImage")  val urlToImage: String?,
    @SerializedName("publishedAt") val publishedAt: String,
    @SerializedName("content")     val content: String?
)
```

> `@SerializedName` digunakan agar nama field Kotlin bisa berbeda dari nama JSON dari API.

**`NewsResponse.kt`** — Wrapper response dari API:

```kotlin
data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)
```

---

### Langkah 4 — Buat API Service (Retrofit Interface)

**`ApiService.kt`** — Mendefinisikan endpoint NewsAPI:

```kotlin
interface ApiService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country")  country: String = "us",
        @Query("category") category: String = "",
        @Query("apiKey")   apiKey: String
    ): NewsResponse

    @GET("everything")
    suspend fun searchNews(
        @Query("q")        query: String,
        @Query("sortBy")   sortBy: String = "publishedAt",
        @Query("language") language: String = "en",
        @Query("apiKey")   apiKey: String
    ): NewsResponse
}
```

> Kata kunci `suspend` membuat fungsi ini bisa dipanggil dari **Kotlin Coroutine** tanpa memblokir main thread.

---

### Langkah 5 — Buat Retrofit Client

**`RetrofitClient.kt`** — Singleton instance Retrofit:

```kotlin
object RetrofitClient {
    private const val BASE_URL = "https://newsapi.org/v2/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
```

> Menggunakan `object` (singleton) dan `by lazy` agar instance Retrofit hanya dibuat satu kali.

---

### Langkah 6 — Buat Repository

**`NewsRepository.kt`** — Lapisan abstraksi antara ViewModel dan API:

```kotlin
class NewsRepository {
    private val apiKey = "YOUR_API_KEY"

    suspend fun getTopHeadlines(category: String = ""): List<Article> {
        val response = if (category.isBlank() || category == "All") {
            RetrofitClient.apiService.getTopHeadlines(apiKey = apiKey)
        } else {
            RetrofitClient.apiService.getTopHeadlines(
                category = category.lowercase(),
                apiKey = apiKey
            )
        }
        // Filter artikel yang sudah dihapus dari API
        return response.articles.filter {
            it.title != "[Removed]" && it.title.isNotBlank()
        }
    }

    suspend fun searchNews(query: String): List<Article> {
        return RetrofitClient.apiService
            .searchNews(query = query, apiKey = apiKey)
            .articles
            .filter { it.title != "[Removed]" && it.title.isNotBlank() }
    }
}
```

---

### Langkah 7 — Buat UI State

**`NewsUiState.kt`** — Sealed class untuk merepresentasikan 3 kemungkinan state UI:

```kotlin
sealed class NewsUiState {
    object Loading : NewsUiState()

    data class Success(
        val articles: List<Article>
    ) : NewsUiState()

    data class Error(
        val message: String
    ) : NewsUiState()
}
```

> `sealed class` memastikan semua kemungkinan state sudah ditangani saat menggunakan `when`.

---

### Langkah 8 — Buat ViewModel

**`NewsViewModel.kt`** — Pusat logika bisnis menggunakan StateFlow:

```kotlin
class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()

    // State utama untuk list berita
    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    // Artikel yang sedang dipilih untuk ditampilkan di Detail
    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle = _selectedArticle.asStateFlow()

    // Set URL artikel yang di-bookmark
    private val _bookmarkedUrls = MutableStateFlow<Set<String>>(emptySet())
    val bookmarkedUrls = _bookmarkedUrls.asStateFlow()

    // List artikel yang di-bookmark (untuk ditampilkan)
    private val _bookmarkedArticles = MutableStateFlow<List<Article>>(emptyList())
    val bookmarkedArticles = _bookmarkedArticles.asStateFlow()

    init { loadNews() }

    fun loadNews(category: String = "All") {
        viewModelScope.launch {          // Coroutine dalam scope ViewModel
            _uiState.value = NewsUiState.Loading
            try {
                val articles = repository.getTopHeadlines(category)
                _uiState.value = NewsUiState.Success(articles)
            } catch (e: Exception) {
                _uiState.value = NewsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun toggleBookmark(article: Article) {
        val currentUrls = _bookmarkedUrls.value.toMutableSet()
        if (article.url in currentUrls) {
            currentUrls.remove(article.url)
            _bookmarkedArticles.value =
                _bookmarkedArticles.value.filter { it.url != article.url }
        } else {
            currentUrls.add(article.url)
            _bookmarkedArticles.value = _bookmarkedArticles.value + article
        }
        _bookmarkedUrls.value = currentUrls
    }
}
```

> - `viewModelScope.launch` menjalankan coroutine yang otomatis dibatalkan jika ViewModel dihancurkan.
> - `MutableStateFlow` adalah observable state holder; UI otomatis update saat value berubah.
> - `.asStateFlow()` mengekspos versi read-only ke UI agar state tidak bisa diubah dari luar.

---

### Langkah 9 — Buat Komponen NewsCard

**`NewsCard.kt`** — Kartu berita dengan tombol share, bookmark, dan more:

```kotlin
@Composable
fun NewsCard(
    article: Article,
    onClick: () -> Unit,
    isBookmarked: Boolean = false,
    onBookmarkClick: (() -> Unit)? = null,
    onShareClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Teks di kiri
            Column(modifier = Modifier.weight(1f)) {
                Text(text = article.title, fontWeight = FontWeight.Bold, maxLines = 3)
                // ... source, stats
            }
            // Thumbnail di kanan
            AsyncImage(model = article.urlToImage, ...)
        }

        // Baris aksi: Share | Bookmark | More
        Row(horizontalArrangement = Arrangement.End) {
            IconButton(onClick = { /* share */ }) {
                Icon(Icons.Default.Share, ...)
            }
            IconButton(onClick = { onBookmarkClick?.invoke() }) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Default.Bookmark
                                  else Icons.Default.BookmarkBorder,
                    tint = if (isBookmarked) NewsAccentBlue else NewsGray
                )
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.MoreVert, ...)
            }
        }
    }
}
```

---

### Langkah 10 — Buat Home Screen

**`HomeScreen.kt`** — Menampilkan list berita dengan search dan kategori:

```kotlin
@Composable
fun HomeScreen(viewModel: NewsViewModel, onArticleClick: (Article) -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    Column {
        // Search bar
        TextField(value = searchText, onValueChange = { ... })

        // Filter kategori
        CategoryChipRow(categories = viewModel.categories, ...)

        // Konten berdasarkan state
        when (val state = uiState) {
            is NewsUiState.Loading -> CircularProgressIndicator()

            is NewsUiState.Success -> {
                val bookmarkedUrls by viewModel.bookmarkedUrls.collectAsState()
                LazyColumn {
                    items(state.articles, key = { it.url }) { article ->
                        NewsCard(
                            article = article,
                            onClick = { onArticleClick(article) },
                            isBookmarked = article.url in bookmarkedUrls,
                            onBookmarkClick = { viewModel.toggleBookmark(article) }
                        )
                    }
                }
            }

            is NewsUiState.Error -> {
                Text(state.message)
                Button(onClick = { viewModel.retry() }) { Text("Retry") }
            }
        }
    }
}
```

> `collectAsState()` mengubah `StateFlow` menjadi `State<T>` yang dapat dibaca oleh Compose. Setiap kali value berubah, Composable akan **recompose** otomatis.

---

### Langkah 11 — Buat Detail Screen

**`DetailScreen.kt`** — Menampilkan detail artikel dengan tombol buka browser:

```kotlin
@Composable
fun DetailScreen(article: Article, viewModel: NewsViewModel, onBack: () -> Unit) {
    val context = LocalContext.current

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        // Gambar hero
        AsyncImage(
            model = article.urlToImage,
            modifier = Modifier.fillMaxWidth().height(260.dp)
        )

        // Judul, source, waktu, konten...
        Text(text = article.title, fontSize = 22.sp, fontWeight = FontWeight.Bold)

        // Tombol buka artikel asli di browser
        Button(
            onClick = {
                runCatching {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                    context.startActivity(intent)
                }
            }
        ) {
            Icon(Icons.Default.Launch, contentDescription = null)
            Spacer(modifier = Modifier.size(8.dp))
            Text("Read Full Article")
        }
    }
}
```

---

### Langkah 12 — Setup Navigation

**`NavGraph.kt`** — Navigasi dengan Bottom Navigation Bar:

```kotlin
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val viewModel: NewsViewModel = viewModel()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            // Hanya tampil di Home dan Bookmarks, bukan di Detail
            AnimatedVisibility(visible = currentRoute != "detail") {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Home, ...) },
                        label = { Text("Home") },
                        selected = currentRoute == "home",
                        onClick = { navController.navigate("home") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Bookmark, ...) },
                        label = { Text("Bookmarks") },
                        selected = currentRoute == "bookmarks",
                        onClick = { navController.navigate("bookmarks") }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(navController, startDestination = "home") {
            composable("home") {
                HomeScreen(viewModel) { article ->
                    viewModel.selectArticle(article)   // Simpan artikel ke ViewModel
                    navController.navigate("detail")   // Lalu navigasi
                }
            }
            composable("bookmarks") { BookmarkScreen(viewModel) { ... } }
            composable("detail") {
                // Ambil artikel dari ViewModel (tidak perlu serialisasi)
                val article by viewModel.selectedArticle.collectAsState()
                article?.let { DetailScreen(it, viewModel) { navController.popBackStack() } }
            }
        }
    }
}
```

> **Cara passing data antar screen:** Artikel disimpan ke `selectedArticle` di ViewModel sebelum navigasi. Ini menghindari keharusan serialisasi objek kompleks sebagai navigation argument.

---

### Langkah 13 — Setup MainActivity

**`MainActivity.kt`** — Entry point aplikasi:

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Status bar & nav bar icons putih (dark mode)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }
        setContent {
            Pertemuan14Theme {
                AppNavGraph()
            }
        }
    }
}
```

---

## 🎨 Sistem Warna (Dark Theme)

```kotlin
// Color.kt
val NewsBlack       = Color(0xFF0A0A0A)  // Background utama
val NewsDarkSurface = Color(0xFF141414)  // Surface (bottom nav, card bg)
val NewsCard        = Color(0xFF1A1A1A)  // Card background
val NewsBorder      = Color(0xFF2A2A2A)  // Divider / border
val NewsWhite       = Color(0xFFFFFFFF)  // Teks utama
val NewsGray        = Color(0xFF888888)  // Teks sekunder / ikon non-aktif
val NewsAccentBlue  = Color(0xFF4FC3F7)  // Aksen (bookmark aktif, "Following")
```

---

## 🔧 Dependency yang Digunakan

| Library | Versi | Fungsi |
|---------|-------|--------|
| `lifecycle-viewmodel-compose` | 2.8.7 | Integrasi ViewModel dengan Compose |
| `navigation-compose` | 2.8.9 | Navigasi antar screen |
| `retrofit2` | 2.11.0 | HTTP client untuk REST API |
| `converter-gson` | 2.11.0 | Parse JSON → Kotlin data class |
| `logging-interceptor` | 4.12.0 | Log request/response HTTP |
| `kotlinx-coroutines-android` | 1.8.1 | Asynchronous programming |
| `coil-compose` | 2.7.0 | Load & cache gambar dari URL |
| `material-icons-extended` | 1.7.8 | Ikon Material Design tambahan |

---

## 📡 API Reference

Base URL: `https://newsapi.org/v2/`

| Endpoint | Method | Parameter | Keterangan |
|----------|--------|-----------|------------|
| `/top-headlines` | GET | `country`, `category`, `apiKey` | Berita utama per negara/kategori |
| `/everything` | GET | `q`, `sortBy`, `language`, `apiKey` | Pencarian berita |

Daftar kategori yang tersedia: `business`, `entertainment`, `health`, `science`, `sports`, `technology`

---

## ▶️ Cara Menjalankan

1. Clone / buka project di **Android Studio**
2. Tunggu **Gradle Sync** selesai
3. Pastikan device/emulator sudah tersambung
4. Klik **Run ▶** atau tekan `Shift + F10`

> Pastikan perangkat memiliki koneksi internet agar data berita dapat dimuat dari API.
