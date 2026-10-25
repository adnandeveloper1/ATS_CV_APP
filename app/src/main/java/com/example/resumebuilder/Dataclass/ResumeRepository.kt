package com.example.resume_builder.Dataclass

import com.example.resumebuilder.models.EducationModel
import com.example.resumebuilder.models.ExperienceModel

object ResumeRepository {
        var name: String = ""
        var address: String = ""
        var email: String = ""
        var phone: String = ""
        var role: String = ""
        var summary: String = ""

        val skillsList = mutableListOf<String>() // Added Skills list
        val experienceList = mutableListOf<ExperienceModel>()
        val educationList = mutableListOf<EducationModel>()

    fun clear() {
        name = ""
        address = ""
        email = ""
        phone = ""
        role = ""
        summary = ""
        experienceList.clear()
        educationList.clear()
    }
}