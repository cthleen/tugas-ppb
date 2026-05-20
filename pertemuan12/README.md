# Project Pertemuan 12 - Authentication System dengan Room & Jetpack Compose

## 📌 Deskripsi Project
Aplikasi ini merupakan implementasi sistem autentikasi (Login & Register) yang menggunakan **Room Database** untuk penyimpanan data lokal dan **Jetpack Compose** untuk antarmuka pengguna yang modern dan deklaratif. 

Tujuan utama project ini adalah mendemonstrasikan bagaimana mengelola *data persistence* secara lokal di Android serta menerapkan pola arsitektur **MVVM (Model-View-ViewModel)** bersama **Repository Pattern** untuk memastikan kode yang bersih, terstruktur, dan mudah dipelihara.

## 🛠️ Tech Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Database**: Room Persistence Library
- **Architecture**: MVVM + Repository Pattern
- **Navigation**: Jetpack Navigation Compose
- **Asynchronous**: Kotlin Coroutines & StateFlow

---

## 🚀 Langkah-Langkah Pengerjaan

### 1. Definisi Data Model (Entity)
Membuat entitas `User` yang akan direpresentasikan sebagai tabel di SQLite. Kita menggunakan anotasi `@Entity` untuk mendefinisikan skema tabel dan `@PrimaryKey` dengan `autoGenerate = true` untuk pembuatan ID unik secara otomatis.

```kotlin
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String,
    val password: String
)
```

### 2. Implementasi Data Access Object (DAO)
`UserDao` bertugas menyediakan metode akses data (CRUD). Kita menggunakan query SQL kustom untuk mencocokkan email dan password saat login, serta fungsi `suspend` agar operasi database berjalan secara asinkron.

```kotlin
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registerUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun loginUser(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?
}
```

### 3. Abstraksi Data dengan Repository
`UserRepository` bertindak sebagai sumber kebenaran tunggal (*Single Source of Truth*) yang mengabstraksi sumber data dari ViewModel. Ini memudahkan jika di masa depan kita ingin menambahkan sumber data dari API (Network).

### 4. Logic & State Management (ViewModel)
`AuthViewModel` mengelola logika bisnis autentikasi. Kita menggunakan `StateFlow` untuk memancarkan perubahan status UI (seperti pesan error atau sukses) dan `viewModelScope` untuk memastikan operasi database tidak memblokir UI utama.

```kotlin
fun onRegister() {
    viewModelScope.launch {
        val existing = repository.getUserByEmail(email)
        if (existing != null) {
            _registerResult.value = AuthResult.Error("Email sudah terdaftar!")
        } else {
            repository.register(email, password)
            _registerResult.value = AuthResult.Success
        }
    }
}
```

### 5. Pengembangan UI dengan Jetpack Compose
Membangun antarmuka yang responsif dan modular. Setiap layar (`Login`, `Register`, `Home`) dipisahkan menjadi file tersendiri untuk keterbacaan kode yang lebih baik.

```kotlin
// Contoh implementasi tombol login yang reaktif
Button(
    onClick = viewModel::onLogin,
    modifier = Modifier.fillMaxWidth().height(50.dp),
    shape = RoundedCornerShape(10.dp)
) {
    Text("Log In")
}
```

### 6. Pengaturan Navigasi (Navigation Compose)
Menggunakan `NavHost` untuk mengelola perpindahan antar layar dan memastikan data mengalir dengan benar melalui `ViewModelFactory`.

---

## 📊 Penjelasan Output & Hasil Akhir

### 1. Fitur Registrasi
- **Validasi Input**: Sistem memeriksa apakah email sudah pernah terdaftar di database.
- **Feedback**: Jika berhasil, data tersimpan di Room dan user diarahkan kembali ke Login. Jika gagal (misal: password < 6 karakter), muncul pesan error via Toast/State.

### 2. Fitur Login
- **Autentikasi**: Aplikasi memverifikasi email dan password terhadap data lokal.
- **Session Management**: Setelah login berhasil, rute `Login` dibersihkan dari *backstack* agar user tidak bisa kembali ke halaman login tanpa Logout.

### 3. Fitur Home
- Menampilkan status keberhasilan autentikasi.
- Menyediakan fungsi **Logout** yang akan mereset status aplikasi dan kembali ke rute awal.

### 4. Data Persistence
- Karena menggunakan **Room**, data pengguna bersifat permanen. Meskipun aplikasi dihentikan paksa atau HP di-restart, akun yang telah dibuat tetap tersimpan dalam database internal perangkat.
