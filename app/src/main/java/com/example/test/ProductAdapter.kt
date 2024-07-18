package com.example.test

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.ItemProductBinding

class ProductAdapter(private val context: Context, private var productList: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val databaseHelper = DatabaseHelper(context)

    class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.binding.textViewProductName.text = product.name
        holder.binding.textViewProductInfo.text = product.info
        holder.binding.textViewProductPrice.text = "$${product.price}"
        //holder.binding.textViewProductCategory.text = product.category

        // Get the resource ID from the image name
        val resourceId = context.resources.getIdentifier(product.image, "drawable", context.packageName)
        holder.binding.imageViewProduct.setImageResource(resourceId)

        // Set checkbox state based on the inCart property
        holder.binding.checkBoxProduct.isChecked = product.inCart

        holder.binding.checkBoxProduct.setOnCheckedChangeListener { _, isChecked ->
            product.inCart = isChecked
            databaseHelper.updateCartlist(product.id, isChecked)
        }
    }

    override fun getItemCount() = productList.size

    fun updateList(newList: List<Product>) {
        productList = newList
        notifyDataSetChanged()
    }
}
