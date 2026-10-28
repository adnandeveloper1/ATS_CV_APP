package com.example.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.resumebuilder.R
import com.example.resumebuilder.models.EducationModel

class EducationAdapter(
    private val educationList: MutableList<EducationModel>
) : RecyclerView.Adapter<EducationAdapter.EducationViewHolder>() {

    private lateinit var list: List<EducationModel>

    inner class EducationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCardTitle: TextView = itemView.findViewById(R.id.tvCardTitle)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
        val etSchool: EditText = itemView.findViewById(R.id.etSchool)
        val etDegree: EditText = itemView.findViewById(R.id.etDegree)
        val etDates: EditText = itemView.findViewById(R.id.etEducationDates)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_education_card, parent, false)
        return EducationViewHolder(view)
    }

    override fun onBindViewHolder(holder: EducationViewHolder, position: Int) {
        val item = educationList[position]

        holder.tvCardTitle.text = "Education #${position + 1}"
        holder.etSchool.setText(item.schoolName)
        holder.etDegree.setText(item.degree)
        holder.etDates.setText(item.dates)

        // Keep model updated when user edits text
        holder.etSchool.doAfterTextChanged { item.schoolName = it.toString() }
        holder.etDegree.doAfterTextChanged { item.degree = it.toString() }
        holder.etDates.doAfterTextChanged { item.dates = it.toString() }

        // Hide delete icon if only 1 card remains
        holder.btnDelete.visibility = if (educationList.size > 1) View.VISIBLE else View.GONE

        // Delete card on click
        holder.btnDelete.setOnClickListener {
            val currentPos = holder.adapterPosition
            if (currentPos != RecyclerView.NO_POSITION && educationList.size > 1) {
                educationList.removeAt(currentPos)
                notifyItemRemoved(currentPos)
                notifyItemRangeChanged(currentPos, educationList.size)
            }
        }
    }
    fun updateList(newList: List<EducationModel>) {
        this.list = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = educationList.size
}