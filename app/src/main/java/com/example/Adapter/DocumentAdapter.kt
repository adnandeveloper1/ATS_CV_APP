package com.example.resumebuilder

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Activities.CreativeActivity
import com.example.Activities.Creative_coverletter
import com.example.Activities.MinimalActivity
import com.example.Activities.Minimal_coverletter
import com.example.Activities.ModernActivity
import com.example.Activities.ProfessionalActivity
import com.example.Activities.professionalCoverletter

class DocumentAdapter(private var documentList: List<Document>) :
    RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder>() {

    class DocumentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDocTitle: TextView = itemView.findViewById(R.id.tvDocTitle)
        val tvDocDate: TextView = itemView.findViewById(R.id.tvDocDate)
        val ivDocIcon: ImageView = itemView.findViewById(R.id.ivDocIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_document, parent, false)
        return DocumentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val doc = documentList[position]
        val context = holder.itemView.context

        holder.tvDocTitle.text = doc.title
        holder.tvDocDate.text = "Last updated: ${doc.date}"

        // Clear image tint to preserve vector original colors
        holder.ivDocIcon.imageTintList = null

        // --- ICON SIZE & SCALE EQUALIZATION FIX ---
        if (doc.isResume) {
            holder.ivDocIcon.setImageResource(R.drawable.resumesave)
            holder.ivDocIcon.scaleType = ImageView.ScaleType.FIT_XY
            holder.ivDocIcon.setPadding(0, 0, 0, 0)
        } else {
            holder.ivDocIcon.setImageResource(R.drawable.coverletter)
            holder.ivDocIcon.scaleType = ImageView.ScaleType.FIT_CENTER
            val density = context.resources.displayMetrics.density
            val paddingInDp = (3 * density).toInt()

            holder.ivDocIcon.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp)
        }

        // --- DYNAMIC DATA & DIRECT ROUTING ON CLICK ---
        holder.itemView.setOnClickListener {
            val typeClean = doc.templateType.lowercase()

            if (doc.isResume) {
                // Route directly to saved preview resume template activity
                val targetActivity = when {
                    typeClean.contains("creative") || typeClean == "template4" -> CreativeActivity::class.java
                    typeClean.contains("modern") || typeClean == "template2" -> ModernActivity::class.java
                    typeClean.contains("minimal") || typeClean == "template3" -> MinimalActivity::class.java
                    typeClean.contains("professional") || typeClean == "template1" -> ProfessionalActivity::class.java
                    else -> ProfessionalActivity::class.java
                }

                val intent = Intent(context, targetActivity).apply {
                    putExtra("docTitle", doc.title)
                    putExtra("templateType", doc.templateType)
                    putExtra("dataJson", doc.dataJson)
                }
                context.startActivity(intent)
            } else {
                // Route directly to saved preview cover letter template activity
                val targetActivity = when {
                    typeClean.contains("creative") -> Creative_coverletter::class.java
                    typeClean.contains("professional") -> professionalCoverletter::class.java
                    typeClean.contains("minimal") -> Minimal_coverletter::class.java
                    else -> Minimal_coverletter::class.java
                }

                val intent = Intent(context, targetActivity).apply {
                    putExtra("docTitle", doc.title)
                    putExtra("templateType", doc.templateType)
                    putExtra("dataJson", doc.dataJson)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = documentList.size

    fun updateData(newList: List<Document>) {
        documentList = newList
        notifyDataSetChanged()
    }
}