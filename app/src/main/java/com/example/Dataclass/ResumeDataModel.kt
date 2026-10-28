package com.example.resume_builder.Dataclass

import com.example.resumebuilder.models.EducationModel
import com.example.resumebuilder.models.ExperienceModel

data class ResumeDataModel(
    var name: String = "",
    var email: String = "",
    var phone: String = "",
    var address: String = "",
    var role: String = "",
    var summary: String = "",
    var experienceList: List<ExperienceModel> = listOf(),
    var educationList: List<EducationModel> = listOf(),
    var skillsList: List<String> = listOf()
)