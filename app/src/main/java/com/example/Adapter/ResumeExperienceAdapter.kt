package com.example.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.resumebuilder.R
import com.example.resumebuilder.models.ExperienceModel

class ResumeExperienceAdapter(
    private val experienceList: List<ExperienceModel>
) : RecyclerView.Adapter<ResumeExperienceAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJobTitleCompany: TextView = view.findViewById(R.id.tv_job_title_company)
        val tvJobDates: TextView = view.findViewById(R.id.tv_job_dates)
        val tvJobDescription: TextView = view.findViewById(R.id.tv_job_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_experience, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = experienceList[position]

        // 1. Format Job Title & Company -> "Senior HRIS Analyst | CloudScale Solutions"
        val titleText = when {
            item.jobTitle.isNotBlank() && item.company.isNotBlank() -> "${item.jobTitle} | ${item.company}"
            item.jobTitle.isNotBlank() -> item.jobTitle
            else -> item.company
        }
        holder.tvJobTitleCompany.text = titleText

        // 2. Format Dates -> "Oct 2021 – Present"
        val datesText = when {
            item.startdates.isNotBlank() && item.enddates.isNotBlank() -> "${item.startdates} – ${item.enddates}"
            item.startdates.isNotBlank() -> item.startdates
            else -> item.enddates
        }
        holder.tvJobDates.text = datesText

        // 3. Format Description with Bullet Points
        if (item.description.isNotBlank()) {
            holder.tvJobDescription.visibility = View.VISIBLE
            holder.tvJobDescription.text = if (item.description.startsWith("•")) {
                item.description
            } else {
                "• ${item.description}"
            }
        } else {
            holder.tvJobDescription.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = experienceList.size
}