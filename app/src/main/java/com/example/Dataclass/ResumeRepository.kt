package com.example.resume_builder.Dataclass

import com.example.resumebuilder.models.EducationModel
import com.example.resumebuilder.models.ExperienceModel

object ResumeRepository {
    var name: String = ""
    var email: String = ""
    var phone: String = ""
    var address: String = ""
    var role: String = ""
    var summary: String = ""
    var experienceList = mutableListOf<ExperienceModel>()
    var educationList = mutableListOf<EducationModel>()
    var skillsList = mutableListOf<String>()

    fun clearAll() {
        name = ""
        email = ""
        phone = ""
        address = ""
        role = ""
        summary = ""
        experienceList.clear()
        educationList.clear()
        skillsList.clear()
    }

    fun toModel(): ResumeDataModel {
        return ResumeDataModel(
            name = this.name,
            email = this.email,
            phone = this.phone,
            address = this.address,
            role = this.role,
            summary = this.summary,
            experienceList = ArrayList(this.experienceList),
            educationList = ArrayList(this.educationList),
            skillsList = ArrayList(this.skillsList)
        )
    }
}