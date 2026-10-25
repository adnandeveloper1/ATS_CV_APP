package com.example.resumebuilder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
// This import is required to connect to your XML layout and IDs
import com.example.resumebuilder.R

class DocumentAdapter(private var documentList: List<Document>) :
    RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder>() {

    class DocumentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // These IDs match the ones you set in item_document.xml
        val tvDocName: TextView = itemView.findViewById(R.id.tvDocName)
        val tvDocDate: TextView = itemView.findViewById(R.id.tvDocDate)
        val docIcon: ImageView = itemView.findViewById(R.id.docIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        // This links to your item_document.xml layout file
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_document, parent, false)
        return DocumentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val doc = documentList[position]
        holder.tvDocName.text = doc.title
        holder.tvDocDate.text = "Last updated: ${doc.date}"

        // These reference your drawables (resume.png/xml and coverletter.png/xml)
        if (doc.isResume) {
            holder.docIcon.setImageResource(R.drawable.resume)
        } else {
            holder.docIcon.setImageResource(R.drawable.coverletter)
        }
    }

    override fun getItemCount(): Int = documentList.size

    fun updateData(newList: List<Document>) {
        documentList = newList
        notifyDataSetChanged()
    }
}