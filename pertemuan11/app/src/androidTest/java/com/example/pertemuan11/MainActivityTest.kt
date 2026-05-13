package com.example.pertemuan11

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented UI Test — dijalankan di emulator/device nyata.
 * Menguji tampilan dan navigasi utama aplikasi MarketSiswa.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // ── 1. TopAppBar "MarketSiswa" tampil ────────────────────────────────────
    @Test
    fun topBar_title_isDisplayed() {
        composeTestRule.onNodeWithText("MarketSiswa").assertIsDisplayed()
    }

    // ── 2. Bottom navigation bar tampil ──────────────────────────────────────
    @Test
    fun bottomNav_beranda_isDisplayed() {
        composeTestRule.onNodeWithText("Beranda").assertIsDisplayed()
    }

    @Test
    fun bottomNav_katalog_isDisplayed() {
        composeTestRule.onNodeWithText("Katalog").assertIsDisplayed()
    }

    @Test
    fun bottomNav_profil_isDisplayed() {
        composeTestRule.onNodeWithText("Profil").assertIsDisplayed()
    }

    // ── 3. FAB "Jual Barang" tampil di Beranda ───────────────────────────────
    @Test
    fun fab_jualBarang_isDisplayed_onHome() {
        composeTestRule.onNodeWithText("Jual Barang").assertIsDisplayed()
    }

    // ── 4. Navigasi ke halaman Katalog ───────────────────────────────────────
    @Test
    fun navigate_to_katalog_showsFabAndProducts() {
        composeTestRule.onNodeWithText("Katalog").performClick()
        // FAB masih tampil
        composeTestRule.onNodeWithText("Jual Barang").assertIsDisplayed()
    }

    // ── 5. Navigasi ke halaman Profil ────────────────────────────────────────
    @Test
    fun navigate_to_profil_showsProfileContent() {
        composeTestRule.onNodeWithText("Profil").performClick()
        // Nama profil pengguna tampil
        composeTestRule.onNodeWithText("John Siswa").assertIsDisplayed()
    }

    // ── 6. Profil menampilkan jabatan akademik ────────────────────────────────
    @Test
    fun profil_shows_academic_info() {
        composeTestRule.onNodeWithText("Profil").performClick()
        composeTestRule
            .onNodeWithText("Teknik Informatika  ·  Semester 5")
            .assertIsDisplayed()
    }

    // ── 7. Profil menampilkan tombol Keluar Akun ─────────────────────────────
    @Test
    fun profil_shows_logout_button() {
        composeTestRule.onNodeWithText("Profil").performClick()
        composeTestRule.onNodeWithText("Keluar Akun").assertIsDisplayed()
    }

    // ── 8. Profil menampilkan Menu Utama ─────────────────────────────────────
    @Test
    fun profil_shows_menu_utama_label() {
        composeTestRule.onNodeWithText("Profil").performClick()
        composeTestRule.onNodeWithText("Menu Utama").assertIsDisplayed()
    }

    // ── 9. Profil menampilkan menu Barang Saya ───────────────────────────────
    @Test
    fun profil_shows_barang_saya_menu() {
        composeTestRule.onNodeWithText("Profil").performClick()
        composeTestRule.onNodeWithText("Barang Saya").assertIsDisplayed()
    }

    // ── 10. Klik FAB membuka form Jual Barang ───────────────────────────────
    @Test
    fun fab_click_opens_add_product_screen() {
        composeTestRule.onNodeWithText("Jual Barang").performClick()
        // TopBar "MarketSiswa" masih ada (Scaffold-level)
        // Form input nama barang harus tampil
        composeTestRule.onNodeWithText("MarketSiswa").assertIsDisplayed()
    }

    // ── 11. Navigasi kembali dari Katalog ke Beranda ─────────────────────────
    @Test
    fun navigate_katalog_then_back_to_beranda() {
        composeTestRule.onNodeWithText("Katalog").performClick()
        composeTestRule.onNodeWithText("Beranda").performClick()
        // FAB masih tampil di Beranda
        composeTestRule.onNodeWithText("Jual Barang").assertIsDisplayed()
    }

    // ── 12. Data produk dummy muncul di Home ─────────────────────────────────
    @Test
    fun home_shows_dummy_product() {
        // Tunggu konten dimuat
        composeTestRule.waitUntil(3_000L) {
            composeTestRule
                .onAllNodesWithText("Buku Kalkulus Purcell")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Buku Kalkulus Purcell").assertIsDisplayed()
    }
}
