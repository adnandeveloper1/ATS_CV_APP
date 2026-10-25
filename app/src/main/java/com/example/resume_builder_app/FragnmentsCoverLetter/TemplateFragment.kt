package com.example.resume_builder_app.Adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.resume_builder_app.Activities.TemplateItem
import com.example.resume_builder_app.Adapters.TemplateAdapter
import com.example.resume_builder_app.R

class TemplateFragment : Fragment() {

    private lateinit var rvTemplates: RecyclerView
    private lateinit var templateAdapter: TemplateAdapter
    private var selectedTemplate: TemplateItem? = null

    private val allTemplates = listOf(
        TemplateItem("Professional", R.drawable.atscoverletter1, "Professional"),
        TemplateItem("Modern", R.drawable.atscoverletter2, "Modern"),
        TemplateItem("Minimal", R.drawable.atscoverletter3, "Minimal")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_template, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvTemplates = view.findViewById(R.id.rvTemplates)
        setupTemplatesRecyclerView()
    }

    private fun setupTemplatesRecyclerView() {
        // Default first template select
        if (allTemplates.isNotEmpty()) {
            selectedTemplate = allTemplates[0]
        }

        templateAdapter = TemplateAdapter(allTemplates.toMutableList()) { template ->
            selectedTemplate = template          // ← yeh line zaroori hai
            Toast.makeText(requireContext(), "Selected: ${template.title}", Toast.LENGTH_SHORT).show()
        }

        rvTemplates.layoutManager = GridLayoutManager(requireContext(), 2)
        rvTemplates.adapter = templateAdapter
    }

    fun getSelectedTemplate(): TemplateItem? {
        return selectedTemplate
    }
}