package com.example.test

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.ItemProductCartBinding

class ShoppingCartAdapter(
    private val productList: MutableList<Product>,
    private val onRemove: (Product) -> Unit,
    private val onAmountChange: (Product, Int) -> Unit
) : RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartViewHolder>() {

    class ShoppingCartViewHolder(val binding: ItemProductCartBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingCartViewHolder {
        val binding = ItemProductCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShoppingCartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingCartViewHolder, position: Int) {
        val product = productList[position]
        holder.binding.textViewProductName.text = product.name
        holder.binding.textViewProductInfo.text = product.info
        holder.binding.textViewProductPrice.text = "$${product.price}"
        //holder.binding.textViewProductCategory.text = product.category
        holder.binding.textViewProductAmount.text = product.amount.toString()

        // Get the resource ID from the image name
        val context = holder.binding.root.context
        val resourceId = context.resources.getIdentifier(product.image, "drawable", context.packageName)
        holder.binding.imageViewProduct.setImageResource(resourceId)

        Log.d("ShoppingCartAdapter", "Binding product: $product")

        holder.binding.buttonIncrease.setOnClickListener {
            onAmountChange(product, product.amount + 1)
            Log.d("ShoppingCartAdapter", "Increased amount for product: $product")

        }



        holder.binding.buttonDecrease.setOnClickListener {
            if (product.amount > 1) {
                onAmountChange(product, product.amount - 1)
                Log.d("ShoppingCartAdapter", "Decreased amount for product: $product")
            }


        }

        holder.binding.buttonRemove.setOnClickListener {
            onRemove(product)
            Log.d("ShoppingCartAdapter", "Removed product: $product")
        }
    }

    override fun getItemCount() = productList.size
}
