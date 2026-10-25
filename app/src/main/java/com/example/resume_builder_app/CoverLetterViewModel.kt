package com.example.resume_builder_app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.resume_builder_app.Activities.TemplateItem
import com.example.resume_builder_app.Adapters.JobDetails

class CoverLetterViewModel : ViewModel() {
    var jobDetails: JobDetails? = null
    var selectedTemplate: TemplateItem? = null
}