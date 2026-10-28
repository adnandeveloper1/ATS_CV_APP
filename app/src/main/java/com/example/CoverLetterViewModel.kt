package com.example

import androidx.lifecycle.ViewModel
import com.example.resumebuilder.Activities.TemplateItem
import com.example.resumebuilder.Adapters.JobDetails

class CoverLetterViewModel : ViewModel() {
    var jobDetails: JobDetails? = null
    var selectedTemplate: TemplateItem? = null
}