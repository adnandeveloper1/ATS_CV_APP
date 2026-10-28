package com.example.resumebuilder

data class Document(
    val id: String = System.currentTimeMillis().toString(),
    val title: String,
    val date: String,
    val isResume: Boolean,
    val templateType: String = "minimal", // e.g., "minimal", "modern", "template1"
    val dataJson: String = ""
)
