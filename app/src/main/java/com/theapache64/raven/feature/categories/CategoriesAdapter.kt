package com.theapache64.raven.feature.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theapache64.raven.data.remote.Category
import com.theapache64.raven.databinding.ItemCategoryBinding

/**
 * Created by theapache64 : Sep 30 Wed,2020 @ 21:49
 */
class CategoriesAdapter(
    private val categories: List<Category>,
    private val callback : Callback
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.category = categories[position]
    }

    override fun getItemCount(): Int = categories.size

    inner class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                callback.onCategoryClicked(layoutPosition)
            }
        }
    }

    interface Callback{
        fun onCategoryClicked(position: Int)
    }
}
