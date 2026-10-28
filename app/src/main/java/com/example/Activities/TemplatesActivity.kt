package com.example.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.resumebuilder.Activities.Category
import com.example.resumebuilder.Activities.TemplateItem
import com.example.resumebuilder.Adapters.CategoryAdapter
import com.example.resumebuilder.Adapters.TemplateAdapter
import com.example.resumebuilder.R

class TemplatesActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var tvTitle: TextView
    private lateinit var rvCategories: RecyclerView
    private lateinit var rvTemplates: RecyclerView

    private lateinit var templateAdapter: TemplateAdapter

    private var allTemplates = listOf<TemplateItem>()
    private var passedDataJson: String = ""
    private var passedDocTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Capture data payload from Intent
        passedDataJson = intent.getStringExtra("dataJson") ?: ""
        passedDocTitle = intent.getStringExtra("docTitle") ?: ""
        val templateType = intent.getStringExtra("templateType") ?: ""

        // Direct redirection ONLY if opened from MyDocs item
        val isDirectSavedViewFlow = intent.getBooleanExtra("isDirectView", false)
        if (isDirectSavedViewFlow && (passedDataJson.isNotEmpty() || templateType.isNotEmpty())) {
            if (openSavedTemplateActivity(templateType, passedDataJson)) {
                finish()
                return
            }
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_templates)

        val mainView = findViewById<ConstraintLayout>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnBack = findViewById(R.id.btnBack)
        tvTitle = findViewById(R.id.tvTitle)
        rvCategories = findViewById(R.id.rvCategories)
        rvTemplates = findViewById(R.id.rvTemplates)

        setupHeader()
        setupMasterData()
        setupTemplatesRecyclerView()
        setupCategoriesRecyclerView()
    }

    private fun openSavedTemplateActivity(templateType: String, dataJson: String): Boolean {
        val cleanType = templateType.trim().lowercase()

        val targetClass = when {
            cleanType.contains("creative") || cleanType == "template4" -> CreativeActivity::class.java
            cleanType.contains("modern") || cleanType == "template2" -> ModernActivity::class.java
            cleanType.contains("minimal") || cleanType == "template3" -> MinimalActivity::class.java
            cleanType.contains("professional") || cleanType == "template1" -> ProfessionalActivity::class.java
            else -> ProfessionalActivity::class.java
        }

        val intent = Intent(this, targetClass).apply {
            putExtra("docTitle", passedDocTitle)
            putExtra("templateType", templateType)
            putExtra("dataJson", dataJson)
        }
        startActivity(intent)
        return true
    }

    private fun setupHeader() {
        btnBack.setOnClickListener { finish() }
    }

    private fun setupMasterData() {
        allTemplates = listOf(
            TemplateItem("Professional 1", R.drawable.professional, "Professional"),
            TemplateItem("Modern 1", R.drawable.ats, "Modern"),
            TemplateItem("Minimal 1", R.drawable.ats3, "Minimal"),
            TemplateItem("Creative 1", R.drawable.ats2, "Creative")
        )
    }

    private fun setupTemplatesRecyclerView() {
        templateAdapter = TemplateAdapter(allTemplates.toMutableList()) { selectedTemplate ->
            val targetClass = when (selectedTemplate.name) {
                "Professional 1" -> ProfessionalActivity::class.java
                "Modern 1" -> ModernActivity::class.java
                "Minimal 1" -> MinimalActivity::class.java
                "Creative 1" -> CreativeActivity::class.java
                else -> ProfessionalActivity::class.java
            }

            val intent = Intent(this, targetClass).apply {
                putExtra("docTitle", passedDocTitle)
                putExtra("templateType", selectedTemplate.name)
                putExtra("dataJson", passedDataJson)
            }
            startActivity(intent)
        }

        rvTemplates.layoutManager = GridLayoutManager(this, 2)
        rvTemplates.adapter = templateAdapter
    }

    private fun setupCategoriesRecyclerView() {
        val categoryList = listOf(
            Category("All", true),
            Category("Professional"),
            Category("Modern"),
            Category("Minimal"),
            Category("Creative")
        )

        val categoryAdapter = CategoryAdapter(categoryList) { selectedCategory ->
            filterTemplates(selectedCategory.name)
        }

        rvCategories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvCategories.adapter = categoryAdapter
    }

    private fun filterTemplates(categoryName: String) {
        val filteredList = if (categoryName.equals("All", ignoreCase = true)) {
            allTemplates
        } else {
            allTemplates.filter { it.category.equals(categoryName, ignoreCase = true) }
        }

        templateAdapter.updateList(filteredList)
    }
}