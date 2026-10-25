package com.example.resume_builder_app.Activities

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
import com.example.resume_builder_app.Adapters.CategoryAdapter
import com.example.resume_builder_app.Adapters.TemplateAdapter
import com.example.resume_builder_app.R


class TemplatesActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var tvTitle: TextView
    private lateinit var rvCategories: RecyclerView
    private lateinit var rvTemplates: RecyclerView

    private lateinit var templateAdapter: TemplateAdapter

    // Store all templates in a master list
    private var allTemplates = listOf<TemplateItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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



    private fun setupHeader() {
        btnBack.setOnClickListener { finish() }
    }

    private fun setupMasterData() {
        // Tag every item with its title and category name
        allTemplates = listOf(
            TemplateItem("Professional 1", R.drawable.professional, "Professional"),
            TemplateItem("Modern 1", R.drawable.ats, "Modern"),
            TemplateItem("Minimal 1", R.drawable.ats3, "Minimal"),
            TemplateItem("Creative 1", R.drawable.ats2, "Creative")
        )
    }

    private fun setupTemplatesRecyclerView() {
        templateAdapter = TemplateAdapter(allTemplates.toMutableList()) { selectedTemplate ->
            when (selectedTemplate.name) {
                "Professional 1" -> {
                    val intent = Intent(this, ProfessionalActivity::class.java)
                    startActivity(intent)

                }
                "Modern 1" -> {
                    val intent = Intent(this, ModernActivity::class.java)
                    startActivity(intent)
                }
                "Minimal 1" -> {
                   val intent = Intent(this, MinimalActivity::class.java)
                    startActivity(intent)
                }
                "Creative 1" -> {
                    val intent = Intent(this, CreativeActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    val intent = Intent(this, ProfessionalActivity::class.java)
                    startActivity(intent)
                }
            }
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