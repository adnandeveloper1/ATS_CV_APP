package com.example.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.resumebuilder.R
import com.example.resumebuilder.models.EducationModel

class ResumeEducationAdapter(
    private val educationList: List<EducationModel>
) : RecyclerView.Adapter<ResumeEducationAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDegreeUniversity: TextView = view.findViewById(R.id.tv_degree_university)
        val tvEduDates: TextView = view.findViewById(R.id.tv_edu_dates)
        val tvEduDetails: TextView = view.findViewById(R.id.tv_edu_details)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_education, parent, false) // Using your item_education XML
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = educationList[position]

        // 1. Format Degree & University -> "BS Computer Science | Iqra University"
        val degreeUniText = when {
            item.degree.isNotBlank() && item.schoolName.isNotBlank() -> "${item.degree} | ${item.schoolName}"
            item.degree.isNotBlank() -> item.degree
            else -> item.schoolName
        }
        holder.tvDegreeUniversity.text = degreeUniText

        // 2. Set Graduation / Education Dates
        holder.tvEduDates.text = item.dates

        // 3. Optional Details / Grade / CGPA field (if available)
        holder.tvEduDetails.visibility = View.GONE
    }

    override fun getItemCount(): Int = educationList.size
}