package com.example.resume_builder.Dataclass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.resume_builder_app.models.EducationModel
import com.example.resume_builder_app.models.ExperienceModel

class SharedResumeViewModel : ViewModel() {

    val name = MutableLiveData<String>()
    val address = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val phone = MutableLiveData<String>()
    val role = MutableLiveData<String>()
    val summary = MutableLiveData<String>()

    private val _experienceList = MutableLiveData<List<ExperienceModel>>(emptyList())
    val experienceList: LiveData<List<ExperienceModel>> = _experienceList

    private val _educationList = MutableLiveData<List<EducationModel>>(emptyList())
    val educationList: LiveData<List<EducationModel>> = _educationList

    fun addExperience(item: ExperienceModel) {
        val current = _experienceList.value.orEmpty().toMutableList()
        current.add(item)
        _experienceList.value = current
    }

    fun addEducation(item: EducationModel) {
        val current = _educationList.value.orEmpty().toMutableList()
        current.add(item)
        _educationList.value = current
    }
}