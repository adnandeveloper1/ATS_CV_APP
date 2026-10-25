package com.example.resumebuilder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.resume_builder.Dataclass.ResumeRepository
import com.example.resumebuilder.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class SkillsFragment : Fragment() {

    private val suggestions = listOf(
        "Kotlin", "Java", "Android SDK", "Jetpack Compose",
        "REST APIs", "Git", "SQLite", "Firebase", "UI/UX Design", "Problem Solving"
    )

    private lateinit var etSkillInput: EditText
    private lateinit var chipGroupAdded: ChipGroup
    private lateinit var chipGroupSuggestions: ChipGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_skills, container, false)

        etSkillInput = view.findViewById(R.id.etSkillInput)
        chipGroupAdded = view.findViewById(R.id.chipGroupAddedSkills)
        chipGroupSuggestions = view.findViewById(R.id.chipGroupSuggestions)
        val btnAddSkill = view.findViewById<Button>(R.id.btnAddSkill)

        // Restore previously added skills from ResumeRepository
        restoreSavedSkills()

        btnAddSkill.setOnClickListener {
            addSkillFromInput()
        }

        etSkillInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addSkillFromInput()
                true
            } else false
        }

        setupSuggestions()

        return view
    }

    private fun restoreSavedSkills() {
        chipGroupAdded.removeAllViews()
        ResumeRepository.skillsList.forEach { skillName ->
            createAndAddChip(skillName)
        }
    }

    private fun addSkillFromInput() {
        val skillText = etSkillInput.text.toString().trim()
        if (skillText.isNotEmpty() && !ResumeRepository.skillsList.contains(skillText)) {
            ResumeRepository.skillsList.add(skillText)
            createAndAddChip(skillText)
            etSkillInput.setText("")
        }
    }

    private fun createAndAddChip(skillName: String) {
        val chip = Chip(requireContext()).apply {
            text = skillName
            isCloseIconVisible = true
            setChipBackgroundColorResource(R.color.dot_active)
            setTextColor(resources.getColor(R.color.black, null))

            setOnCloseIconClickListener {
                chipGroupAdded.removeView(this)
                ResumeRepository.skillsList.remove(skillName)
            }
        }
        chipGroupAdded.addView(chip)
    }

    private fun setupSuggestions() {
        chipGroupSuggestions.removeAllViews()
        suggestions.forEach { suggestion ->
            val chip = Chip(requireContext()).apply {
                text = "+ $suggestion"
                isCheckable = false

                setOnClickListener {
                    if (!ResumeRepository.skillsList.contains(suggestion)) {
                        ResumeRepository.skillsList.add(suggestion)
                        createAndAddChip(suggestion)
                    }
                }
            }
            chipGroupSuggestions.addView(chip)
        }
    }
}