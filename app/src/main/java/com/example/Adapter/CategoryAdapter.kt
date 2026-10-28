package com.example.resumebuilder.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.resumebuilder.Activities.Category
import com.example.resumebuilder.R
import com.google.android.material.button.MaterialButton

class CategoryAdapter(
    private val categories: List<Category>,
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition = 0

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnCategory: MaterialButton = itemView.findViewById(R.id.btnCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_chip, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        val isSelected = position == selectedPosition

        holder.btnCategory.text = category.name

        if (isSelected) {
            holder.btnCategory.setBackgroundColor(Color.parseColor("#1D61E8"))
            holder.btnCategory.setTextColor(Color.WHITE)
            holder.btnCategory.strokeWidth = 0
        } else {
            holder.btnCategory.setBackgroundColor(Color.TRANSPARENT)
            holder.btnCategory.setTextColor(Color.parseColor("#64748B"))
            holder.btnCategory.strokeWidth = 2
        }

        holder.btnCategory.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.bindingAdapterPosition
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            onCategoryClick(category)
        }
    }

    override fun getItemCount(): Int = categories.size
}