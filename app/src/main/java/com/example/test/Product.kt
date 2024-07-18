package com.example.test

data class Product(
    val id: Int,
    val name: String,
    val info: String,
    val image: String,
    val price: Double,
    val category: String,
    var inCart: Boolean = false,
    var amount: Int = 1 // New property for quantity
)
data class Item(
    val name: String,
    val info: String,
    val price: Double,
    val rating: Float,
    val amount: Int
)