package com.example.pertemuan11

import com.example.pertemuan11.model.Product
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit Test untuk model Product.
 * Dijalankan di JVM lokal (tanpa emulator).
 */
class ProductUnitTest {

    // ── Helper: membuat produk sampel ──────────────────────────────────────────
    private fun sampleProduct() = Product(
        name        = "Buku Kalkulus Purcell",
        price       = "75.000",
        category    = "Buku",
        description = "Kondisi 90% mulus"
    )

    // ── 1. Properti default ────────────────────────────────────────────────────
    @Test
    fun product_defaultSeller_isJohnSiswa() {
        val product = sampleProduct()
        assertEquals("John Siswa", product.seller)
    }

    @Test
    fun product_defaultRating_isFourPointNine() {
        val product = sampleProduct()
        assertEquals(4.9, product.rating, 0.001)
    }

    @Test
    fun product_defaultBrand_isMarketSiswa() {
        val product = sampleProduct()
        assertEquals("MarketSiswa", product.brand)
    }

    @Test
    fun product_defaultColor_isModern() {
        val product = sampleProduct()
        assertEquals("Modern", product.color)
    }

    // ── 2. Properti wajib tersimpan dengan benar ───────────────────────────────
    @Test
    fun product_name_isSetCorrectly() {
        val product = sampleProduct()
        assertEquals("Buku Kalkulus Purcell", product.name)
    }

    @Test
    fun product_price_isSetCorrectly() {
        val product = sampleProduct()
        assertEquals("75.000", product.price)
    }

    @Test
    fun product_category_isSetCorrectly() {
        val product = sampleProduct()
        assertEquals("Buku", product.category)
    }

    @Test
    fun product_description_isSetCorrectly() {
        val product = sampleProduct()
        assertEquals("Kondisi 90% mulus", product.description)
    }

    // ── 3. ID unik setiap produk baru ─────────────────────────────────────────
    @Test
    fun product_id_isUnique_betweenTwoInstances() {
        val p1 = Product(name = "A", price = "10", category = "X", description = "desc")
        Thread.sleep(2) // pastikan timestamp berbeda
        val p2 = Product(name = "B", price = "20", category = "Y", description = "desc")
        assertNotEquals(p1.id, p2.id)
    }

    // ── 4. copy() mengubah field yang ditentukan ───────────────────────────────
    @Test
    fun product_copy_changesOnlySpecifiedFields() {
        val original = sampleProduct()
        val updated  = original.copy(price = "60.000", seller = "Jane Siswa")

        assertEquals("60.000",    updated.price)
        assertEquals("Jane Siswa", updated.seller)
        // Field lain tidak berubah
        assertEquals(original.name,        updated.name)
        assertEquals(original.category,    updated.category)
        assertEquals(original.description, updated.description)
        assertEquals(original.brand,       updated.brand)
    }

    // ── 5. Equality (data class) ───────────────────────────────────────────────
    @Test
    fun product_sameData_areEqual() {
        val fixedId = 123456789L
        val p1 = Product(id = fixedId, name = "Buku", price = "50", category = "Buku", description = "ok")
        val p2 = Product(id = fixedId, name = "Buku", price = "50", category = "Buku", description = "ok")
        assertEquals(p1, p2)
    }

    @Test
    fun product_differentName_areNotEqual() {
        val fixedId = 987654321L
        val p1 = Product(id = fixedId, name = "Buku A", price = "50", category = "Buku", description = "ok")
        val p2 = Product(id = fixedId, name = "Buku B", price = "50", category = "Buku", description = "ok")
        assertNotEquals(p1, p2)
    }

    // ── 6. toString() mengandung field penting ────────────────────────────────
    @Test
    fun product_toString_containsName() {
        val product = sampleProduct()
        assertTrue(product.toString().contains("Buku Kalkulus Purcell"))
    }

    // ── 7. Rating tidak negatif ────────────────────────────────────────────────
    @Test
    fun product_rating_isNonNegative() {
        val product = sampleProduct()
        assertTrue("Rating harus >= 0", product.rating >= 0.0)
    }

    // ── 8. Price string tidak kosong ──────────────────────────────────────────
    @Test
    fun product_price_isNotEmpty() {
        val product = sampleProduct()
        assertTrue(product.price.isNotBlank())
    }

    // ── 9. Name string tidak kosong ───────────────────────────────────────────
    @Test
    fun product_name_isNotEmpty() {
        val product = sampleProduct()
        assertTrue(product.name.isNotBlank())
    }

    // ── 10. Custom seller dapat di-set ────────────────────────────────────────
    @Test
    fun product_customSeller_isSetCorrectly() {
        val product = Product(
            name        = "Sepatu",
            price       = "100.000",
            category    = "Sepatu",
            description = "Kondisi baru",
            seller      = "Alice"
        )
        assertEquals("Alice", product.seller)
    }
}
