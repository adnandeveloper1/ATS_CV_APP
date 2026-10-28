package com.example.resumebuilder.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.resumebuilder.Activities.TemplateItem
import com.example.resumebuilder.R
import com.google.android.material.card.MaterialCardView

class TemplateAdapter(
    private val templates: MutableList<TemplateItem>,
    private val onTemplateClick: (TemplateItem) -> Unit
) : RecyclerView.Adapter<TemplateAdapter.TemplateViewHolder>() {

    private var selectedPosition = 0

    class TemplateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardTemplate: MaterialCardView = itemView.findViewById(R.id.cardTemplate)
        val imgTemplate: ImageView = itemView.findViewById(R.id.imgTemplate)
        val tvTemplateName: TextView = itemView.findViewById(R.id.tvTemplateName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_template_card, parent, false)
        return TemplateViewHolder(view)
    }

    override fun onBindViewHolder(holder: TemplateViewHolder, position: Int) {
        val item = templates[position]
        val isSelected = position == selectedPosition

        holder.tvTemplateName.text = item.name
        holder.imgTemplate.setImageResource(item.imageResId)

        if (isSelected) {
            holder.cardTemplate.strokeWidth = 6
            holder.cardTemplate.strokeColor = Color.parseColor("#1D61E8")
        } else {
            holder.cardTemplate.strokeWidth = 0
        }

        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.bindingAdapterPosition
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            onTemplateClick(item)
        }
    }

    override fun getItemCount(): Int = templates.size

    // Call this function from TemplatesActivity to update the filtered list
    fun updateList(newList: List<TemplateItem>) {
        templates.clear()
        templates.addAll(newList)
        selectedPosition = 0 // Reset selection to first item when list changes
        notifyDataSetChanged()
    }
}