package com.example.resumebuilder.Activities

data class Category(
    val name: String,
    var isSelected: Boolean = false
)

data class TemplateItem(
    val name: String,
    val imageResId: Int, // Set to your drawable resource ID when ready
    val category: String,
    val id: Int = 0
) {
    val title: String get() = name
    val type: String get() = category
}