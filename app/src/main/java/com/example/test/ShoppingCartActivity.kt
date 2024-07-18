package com.example.test

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ShoppingCartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var totalPriceTextView: TextView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var shoppingCartAdapter: ShoppingCartAdapter
    private var selectedProducts = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_cart)

        recyclerView = findViewById(R.id.recyclerViewShoppingCart)
        totalPriceTextView = findViewById(R.id.totalPrice)
        databaseHelper = DatabaseHelper(this)

        loadSelectedProducts()

        shoppingCartAdapter = ShoppingCartAdapter(selectedProducts, this::removeProduct, this::changeAmount)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = shoppingCartAdapter

        updateTotalPrice()

    }

    private fun loadSelectedProducts() {
        selectedProducts = databaseHelper.getCartProducts().toMutableList()
        Log.d("ShoppingCartActivity", "Selected products: $selectedProducts")
    }

    private fun saveSelectedProducts() {
        selectedProducts.forEach { product ->
            databaseHelper.updateCartlist(product.id, product.inCart)
        }
    }

    private fun removeProduct(product: Product) {
        selectedProducts.remove(product)
        saveSelectedProducts()
        shoppingCartAdapter.notifyDataSetChanged()
        updateTotalPrice()
        Log.d("ShoppingCartActivity", "Removed product: $product")
    }

    private fun changeAmount(product: Product, amount: Int) {
        product.amount = amount
        saveSelectedProducts()
        shoppingCartAdapter.notifyDataSetChanged()
        updateTotalPrice()
        Log.d("ShoppingCartActivity", "Changed amount for product: $product to $amount")
    }

    private fun updateTotalPrice() {
        val totalPrice = selectedProducts.sumOf { it.price * it.amount }
        totalPriceTextView.text = "$$totalPrice"
        Log.d("ShoppingCartActivity", "Total price: $totalPrice")
    }

}
