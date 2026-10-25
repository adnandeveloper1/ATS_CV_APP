package com.example.resumebuilder.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.resumebuilder.R
import com.example.resumebuilder.models.ExperienceModel

class ExperienceAdapter(
    private val experienceList: MutableList<ExperienceModel>
) : RecyclerView.Adapter<ExperienceAdapter.ExperienceViewHolder>() {

    inner class ExperienceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCardTitle: TextView = itemView.findViewById(R.id.tvCardTitle)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
        val etJobTitle: EditText = itemView.findViewById(R.id.etJobTitle)
        val etCompany: EditText = itemView.findViewById(R.id.etCompany)
        val etstartDates: EditText = itemView.findViewById(R.id.etstartDates)
        val etenddate: EditText = itemView.findViewById(R.id.etEndDates)
        val etDescription: EditText = itemView.findViewById(R.id.etDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_experience_card, parent, false)
        return ExperienceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {
        val item = experienceList[position]

        holder.tvCardTitle.text = "Experience #${position + 1}"

        // Populate existing values without triggering infinite text watcher loops
        holder.etJobTitle.setText(item.jobTitle)
        holder.etCompany.setText(item.company)
        holder.etstartDates.setText(item.startdates)
        holder.etenddate.setText(item.enddates)
        holder.etDescription.setText(item.description)

        // Real-time synchronization back to ResumeRepository
        holder.etJobTitle.doAfterTextChanged { item.jobTitle = it.toString() }
        holder.etCompany.doAfterTextChanged { item.company = it.toString() }
        holder.etstartDates.doAfterTextChanged { item.startdates = it.toString() }
        holder.etenddate.doAfterTextChanged { item.enddates = it.toString() }
        holder.etDescription.doAfterTextChanged { item.description = it.toString() }

        holder.btnDelete.visibility = if (experienceList.size > 1) View.VISIBLE else View.GONE

        holder.btnDelete.setOnClickListener {
            val currentPos = holder.adapterPosition
            if (currentPos != RecyclerView.NO_POSITION && experienceList.size > 1) {
                experienceList.removeAt(currentPos)
                notifyItemRemoved(currentPos)
                notifyItemRangeChanged(currentPos, experienceList.size)
            }
        }
    }

    fun updateList(newList: List<ExperienceModel>) {
        experienceList.clear()
        experienceList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = experienceList.size
}