package com.example.resumebuilder.ResumeFragnments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.resume_builder.Dataclass.ResumeRepository
import com.example.Adapter.ExperienceAdapter
import com.example.resumebuilder.R
import com.example.resumebuilder.models.ExperienceModel

class ExperienceFragment : Fragment() {

    private lateinit var adapter: ExperienceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_experience, container, false)

        val rvExperience = view.findViewById<RecyclerView>(R.id.rvExperience)
        val btnAddExperience = view.findViewById<Button>(R.id.btnAddExperience)

        // Ensure there is at least one item in the list initially
        if (ResumeRepository.experienceList.isEmpty()) {
            ResumeRepository.experienceList.add(ExperienceModel())
        }

        // Pass ResumeRepository's master list directly to the adapter
        adapter = ExperienceAdapter(ResumeRepository.experienceList)
        rvExperience.layoutManager = LinearLayoutManager(requireContext())
        rvExperience.adapter = adapter

        // Add exactly ONE new empty card when clicking Add Experience
        btnAddExperience.setOnClickListener {
            val newIndex = ResumeRepository.experienceList.size
            ResumeRepository.experienceList.add(ExperienceModel())
            adapter.notifyItemInserted(newIndex)
        }

        return view
    }
}