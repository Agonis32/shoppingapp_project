package com.example.test

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var editTextPrice: EditText
    private lateinit var editTextImageUrl: EditText
    private lateinit var editTextId: EditText
    private lateinit var buttonAdd: Button
    private lateinit var buttonDelete: Button
    private lateinit var buttonViewGroceryList: Button
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var editTextCategory: EditText
    private lateinit var buttonEnglish: Button
    private lateinit var buttonGreek: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", "onCreate: Activity started")

        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)

        editTextName = findViewById(R.id.editTextName)
        editTextDescription = findViewById(R.id.editTextDescription)
        editTextPrice = findViewById(R.id.editTextPrice)
        editTextImageUrl = findViewById(R.id.editTextImageUrl)
        editTextId = findViewById(R.id.editTextId)
        buttonAdd = findViewById(R.id.buttonAdd)
        buttonDelete = findViewById(R.id.buttonDelete)
        buttonViewGroceryList = findViewById(R.id.buttonViewGroceryList)
        editTextCategory = findViewById(R.id.editTextCategory)
        buttonEnglish = findViewById(R.id.buttonEnglish)
        buttonGreek = findViewById(R.id.buttonGreek)

        // setarisma toy text string.xml oste na deixnei ellinika sta koumpia
        buttonAdd.text = getString(R.string.add_product)
        buttonDelete.text = getString(R.string.delete_product)
        buttonViewGroceryList.text = getString(R.string.view_grocery_list)

        // setarisma toy string.xml gia na deixnei ellinika sto keimeno
        editTextName.setText(getString(R.string.name))
        editTextDescription.setText(getString(R.string.description))
        editTextImageUrl.setText(getString(R.string.image_url))
        editTextPrice.setText(getString(R.string.price))
        editTextId.setText(getString(R.string.product_id))
        editTextCategory.setText(getString(R.string.Category))





        buttonAdd.setOnClickListener {
            val name = editTextName.text.toString()
            val description = editTextDescription.text.toString()
            val price = editTextPrice.text.toString().toDoubleOrNull() ?: 0.0
            val imageUrl = editTextImageUrl.text.toString()
            val category = editTextCategory.text.toString().toIntOrNull() ?: 0



            if (name.isNotEmpty() && description.isNotEmpty() && price > 0 && imageUrl.isNotEmpty() && category in 1..4) {
                databaseHelper.insertProduct(name, description, price, imageUrl, category.toString())
                Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }

        buttonDelete.setOnClickListener {
            val id = editTextId.text.toString().toIntOrNull()
            if (id != null) {
                databaseHelper.deleteProduct(id)
                Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a valid product ID", Toast.LENGTH_SHORT).show()
            }
        }

        buttonViewGroceryList.setOnClickListener {
            startActivity(Intent(this, GroceryListActivity::class.java))
        }

        buttonEnglish.setOnClickListener {
            Log.d("MainActivity", "English button clicked")
            setLocale("en")
        }

        buttonGreek.setOnClickListener {
            Log.d("MainActivity", "Greek button clicked")
            setLocale("el")
        }
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        // Save the selected language
        val editor = sharedPreferences.edit()
        editor.putString("My_Lang", languageCode)
        editor.apply()

        // Restart the activity to apply the new language
        val refresh = Intent(this, MainActivity::class.java)
        startActivity(refresh)
        finish()
    }
}
