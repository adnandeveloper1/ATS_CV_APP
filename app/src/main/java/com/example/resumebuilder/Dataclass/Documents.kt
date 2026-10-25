package com.example.resumebuilder

data class Documents(
    val title: String,
    val imageRes: Int,
    val type: String
) {
    val name: String get() = title
    val imageResId: Int get() = imageRes
    val category: String get() = type
}
