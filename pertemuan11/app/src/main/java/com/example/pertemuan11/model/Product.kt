package com.example.pertemuan11.model

data class Product(
    val id: Long = System.currentTimeMillis(),
    val name: String,
    val price: String,
    val category: String,
    val description: String,
    val seller: String = "John Siswa",
    val rating: Double = 4.9,
    val reviews: String = "2.3k+",
    val soldCount: String = "2.5k+",
    val brand: String = "MarketSiswa",
    val color: String = "Modern"
)
