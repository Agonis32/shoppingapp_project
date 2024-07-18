package com.example.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GroceryListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var productAdapter: ProductAdapter
    private var allProducts: List<Product> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grocery_list)

        try {
            recyclerView = findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)

            databaseHelper = DatabaseHelper(this)
            allProducts = databaseHelper.getAllProducts()
            productAdapter = ProductAdapter(this, allProducts)
            recyclerView.adapter = productAdapter



            val searchView = findViewById<SearchView>(R.id.searchView)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filterProducts(newText)
                    return true
                }
            })


            val buttonAll = findViewById<Button>(R.id.buttonAll)
            val buttonDrinks = findViewById<Button>(R.id.buttonDrinks)
            val buttonMilk = findViewById<Button>(R.id.buttonMilk)
            val buttonChocolate = findViewById<Button>(R.id.buttonChocolate)

            buttonAll.text = getString(R.string.All)
            buttonDrinks.text = getString(R.string.Drinks)
            buttonMilk.text = getString(R.string.Milk)
            buttonChocolate.text = getString(R.string.Choco)




            //val buttonBiscuits = findViewById<Button>(R.id.buttonBiscuits)

            buttonAll.setOnClickListener {
                productAdapter.updateList(allProducts)
            }

            buttonDrinks.setOnClickListener {
                filterProductsByCategory("1")
            }

            buttonMilk.setOnClickListener {
                filterProductsByCategory("2")
            }

            buttonChocolate.setOnClickListener {
                filterProductsByCategory("3")
            }
            //buttonBiscuits.setOnClickListener {
                //filterProductsByCategory("4")
            //}

            val buttonShoppingCart = findViewById<Button>(R.id.buttonShoppingCart)
            buttonShoppingCart.setOnClickListener {
                val intent = Intent(this, ShoppingCartActivity::class.java)
                startActivity(intent)

            }
            buttonShoppingCart.text = getString(R.string.ShoppingCart)


        } catch (e: Exception) {
            Log.e("GroceryListActivity", "Error in onCreate", e)
        }



    }


    private fun filterProducts(query: String?) {
        val filteredList = if (!query.isNullOrEmpty()) {
            allProducts.filter { it.name.contains(query, true) || it.info.contains(query, true) }
        } else {
            allProducts
        }
        productAdapter.updateList(filteredList)
    }

    private fun filterProductsByCategory(category: String) {
        val filteredList = allProducts.filter { it.category == category }
        productAdapter.updateList(filteredList)
    }



}
