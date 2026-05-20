# Dokumentasi Proyek MarketSiswa

Berikut adalah penjelasan mengenai pembuatan aplikasi **MarketSiswa**, sebuah platform marketplace mahasiswa berbasis Jetpack Compose:

### 1. Pembuatan Project & Arsitektur
Proyek ini dibuat menggunakan template **Empty Activity (Jetpack Compose)**. Untuk menjaga kerapihan kode dan kemudahan pengembangan, arsitektur proyek dibagi menjadi beberapa modul:
- **`MainActivity.kt`**: Entry point utama yang mengatur navigasi antar halaman menggunakan state-based navigation (tanpa Navigation Component).
- **`model/`**: Menyimpan struktur data produk (`Product.kt`) dengan 15 data dummy bawaan.
- **`ui/screens/`**: Berisi file terpisah untuk setiap halaman:
  - `LoginScreen.kt` — Halaman login dengan form email & password
  - `HomeScreen.kt` — Beranda dengan banner promo dan produk unggulan
  - `CatalogueScreen.kt` — Katalog seluruh barang dalam tampilan Grid
  - `AddProductScreen.kt` — Formulir jual barang baru
  - `ProductDetailScreen.kt` — Detail produk yang dipilih
  - `ProfileScreen.kt` — Profil pengguna dan menu akun
- **`ui/theme/`**: Mengatur konfigurasi warna kustom, tipografi, dan tema Material 3.

### 2. Pembuatan Tampilan (Multi-Screen)
Aplikasi ini memiliki sistem navigasi berbasis state yang dimulai dari halaman Login:
- **Halaman Login**: Tampilan hero dengan gradien Teal, form email & password, serta tombol "Masuk Sekarang" yang langsung mengarahkan ke halaman utama.
- **Halaman Utama (Home)**: Menggunakan `Scaffold` dengan `TopAppBar` dan `BottomNavigationBar` untuk akses ke Beranda, Katalog, dan Profil.
- **Transisi**: Menggunakan `AnimatedContent` untuk memberikan efek animasi halus (fade-in/out) saat berpindah antar menu navigasi maupun saat membuka detail produk.

### 3. Komponen yang Digunakan
Komponen-komponen utama yang digunakan dalam membangun MarketSiswa meliputi:
- **Scaffold & TopAppBar**: Kerangka dasar aplikasi dengan judul "MarketSiswa" di tengah (`CenterAlignedTopAppBar`).
- **OutlinedTextField**: Digunakan untuk input email/password di Login dan form pengisian detail barang saat menjual.
- **Card (Material 3)**: Membungkus informasi produk dengan efek *elevation* dan sudut melingkar (16dp).
- **LazyColumn & LazyVerticalGrid**: Menampilkan daftar barang dalam bentuk list (di Home) dan grid (di Katalog) secara efisien.
- **Floating Action Button (FAB)**: Tombol aksi utama "Jual Barang" (`ExtendedFloatingActionButton`) yang tampil di halaman Beranda dan Katalog.
- **AnimatedContent**: Mengelola transisi halus antar seluruh screen.

### 4. Pengaturan Layout & Estetika
Untuk mencapai tampilan yang modern, bersih, dan sesuai prinsip Material Design 3:
- **Custom Theme**: Menggunakan warna aksen **Teal-Hijau Tua (`#0F7A6B`)** pada tombol, ikon aktif, dan gradien header.
- **Tipografi**: Seluruh aplikasi menggunakan font **Plus Jakarta Sans** (fallback: SansSerif sistem) agar tampilan konsisten dan modern.
- **Spasi & Bentuk**: Menggunakan `Spacer` dan `padding` yang luas untuk memberikan kesan lega (*whitespace*), serta sudut melingkar (**Rounded Corners**) pada semua komponen utama.

### 5. Hasil Akhir
Aplikasi **MarketSiswa** menghasilkan platform marketplace fungsional bagi mahasiswa yang terdiri dari:
- **Login**: Halaman masuk dengan validasi sederhana sebelum mengakses konten utama.
- **Beranda**: Menampilkan banner promo, kategori populer, dan produk unggulan terbaru.
- **Katalog Lengkap**: Daftar seluruh barang kampus yang diperjualbelikan dengan tampilan Grid.
- **Detail Produk**: Informasi lengkap produk (harga, deskripsi, penjual, rating) dengan tombol beli.
- **Fitur Jual**: Formulir untuk menambahkan barang baru dengan simulasi proses *loading*.
- **Profil**: Informasi pengguna (John Siswa — Teknik Informatika, Semester 5) beserta statistik dan menu pengaturan akun.

### 6. Testing dengan Emulator
Proyek ini dilengkapi dengan **Instrumented UI Test** menggunakan Jetpack Compose Testing:
- **File test**: `app/src/androidTest/.../MainActivityTest.kt`
- **Cara menjalankan**:
  1. Pastikan emulator/device sudah aktif di Android Studio
  2. Klik kanan pada file `MainActivityTest.kt` → **Run 'MainActivityTest'**
  3. Atau via terminal: `.\gradlew.bat connectedAndroidTest`
- **Cakupan test**: navigasi antar halaman, tampilan konten profil, FAB "Jual Barang", dan validasi data produk dummy.
