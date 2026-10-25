package com.example.resumebuilder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.resume_builder.Dataclass.ResumeRepository
import com.example.resumebuilder.Adapter.EducationAdapter
import com.example.resumebuilder.R
import com.example.resumebuilder.models.EducationModel

class EducationFragment : Fragment() {

    private lateinit var adapter: EducationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_education, container, false)

        val rvEducation = view.findViewById<RecyclerView>(R.id.rvEducation)
        val btnAddEducation = view.findViewById<Button>(R.id.btnAddEducation)

        // Ensure there is at least one item initially in repository
        if (ResumeRepository.educationList.isEmpty()) {
            ResumeRepository.educationList.add(EducationModel())
        }

        // Connect adapter directly to ResumeRepository list
        adapter = EducationAdapter(ResumeRepository.educationList)
        rvEducation.layoutManager = LinearLayoutManager(requireContext())
        rvEducation.adapter = adapter

        // Add a new empty item on click
        btnAddEducation.setOnClickListener {
            val newIndex = ResumeRepository.educationList.size
            ResumeRepository.educationList.add(EducationModel())
            adapter.notifyItemInserted(newIndex)
        }

        return view
    }
}