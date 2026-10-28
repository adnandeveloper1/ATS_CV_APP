package com.example.resumebuilder

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object DocumentManager {

    private const val PREF_NAME = "ResumeBuilderDocs"
    private const val KEY_DOCS = "saved_documents"

    fun saveDocument(
        context: Context,
        title: String,
        isResume: Boolean,
        templateType: String,
        dataJson: String = ""
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val savedList = getDocuments(context).toMutableList()

        val newDoc = Document(
            id = System.currentTimeMillis().toString(),
            title = title,
            date = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault()).format(java.util.Date()),
            isResume = isResume,
            templateType = templateType,
            dataJson = dataJson
        )

        savedList.add(0, newDoc) // Top par add karein

        val jsonString = Gson().toJson(savedList)
        prefs.edit().putString(KEY_DOCS, jsonString).apply()
    }

    // DocumentManager.kt mein getDocuments function aisa hona chahiye:
    fun getDocuments(context: Context): List<Document> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_DOCS, null) ?: return emptyList() // Agar empty ho toh emptyList hi return karein

        val type = object : TypeToken<List<Document>>() {}.type
        return try {
            Gson().fromJson(jsonString, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun deleteDocument(context: Context, document: Document) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val savedList = getDocuments(context).toMutableList()
        savedList.removeAll { it.id == document.id }

        val jsonString = Gson().toJson(savedList)
        prefs.edit().putString(KEY_DOCS, jsonString).apply()
    }
}