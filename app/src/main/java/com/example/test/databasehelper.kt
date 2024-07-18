package com.example.test

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "shoppingapp.db"
        private const val DATABASE_VERSION = 3 // Incremented database version
        const val TABLE_NAME = "kategory"
        const val COLUMN_ID = "id"
        const val COLUMN_PRODUCT_NAME = "productname"
        const val COLUMN_PRICE = "price"
        const val COLUMN_INFO = "info"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_CARTLIST = "cartlist"
        const val COLUMN_CATEGORY = "category"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PRODUCT_NAME + " TEXT NOT NULL,"
                + COLUMN_INFO + " TEXT,"
                + COLUMN_PRICE + " REAL NOT NULL,"
                + COLUMN_IMAGE + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_CARTLIST + " INTEGER DEFAULT 0" + ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 3) {
            // Rename old table
            db.execSQL("ALTER TABLE grocerylist RENAME TO temp_grocerylist")

            // Create new table
            onCreate(db)

            // Copy data from old table to new table
            db.execSQL("INSERT INTO $TABLE_NAME ($COLUMN_ID, $COLUMN_PRODUCT_NAME, $COLUMN_INFO, $COLUMN_PRICE, $COLUMN_IMAGE, $COLUMN_CARTLIST) " +
                    "SELECT $COLUMN_ID, $COLUMN_PRODUCT_NAME, $COLUMN_INFO, $COLUMN_PRICE, $COLUMN_IMAGE, $COLUMN_CARTLIST FROM temp_grocerylist")

            // Drop the old table
            db.execSQL("DROP TABLE temp_grocerylist")
        }
    }

    fun insertProduct(productname: String, info: String, price: Double, image: String, category: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PRODUCT_NAME, productname)
            put(COLUMN_INFO, info)
            put(COLUMN_PRICE, price)
            put(COLUMN_IMAGE, image)
            put(COLUMN_CATEGORY, category)
            put(COLUMN_CARTLIST, 0) // default value
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateCartlist(id: Int, inCart: Boolean) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CARTLIST, if (inCart) 1 else 0)
        }
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun deleteProduct(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun getAllProducts(): List<Product> {
        val productList = ArrayList<Product>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val product = Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)) ?: "",
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INFO)) ?: "",
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)) ?: "",
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)) ?: "",
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CARTLIST)) == 1
                )
                productList.add(product)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return productList
    }

    fun getCartProducts(): List<Product> {
        val productList = ArrayList<Product>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_CARTLIST = 1", null)
        if (cursor.moveToFirst()) {
            do {
                val product = Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)) ?: "",
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INFO)) ?: "",
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)) ?: "",
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)) ?: "",
                    true
                )
                productList.add(product)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return productList
    }
}
