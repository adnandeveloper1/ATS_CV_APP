package com.example.resumebuilder.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.resumebuilder.Document
import com.example.resumebuilder.DocumentAdapter
import com.example.resumebuilder.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {

    private lateinit var documentAdapter: DocumentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        // Setup Buttons
        setupButtons()

        // Setup RecyclerView for dynamic list
        setupRecyclerView()

        // Load dummy data to test it out
        loadUserDocuments()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    private fun setupButtons() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val fabHome = findViewById<FloatingActionButton>(R.id.fabHome)

        val cardcreatecover = findViewById<ConstraintLayout>(R.id.cardCreateCoverLetter)
        cardcreatecover.setOnClickListener {
            val intent = Intent(this, CoverLetterActivity::class.java)
            startActivity(intent)
        }

        val cardCreateResume = findViewById<ConstraintLayout>(R.id.cardCreateResume)
        cardCreateResume.setOnClickListener {
            val intent = Intent(this, ResumeActivity::class.java)
            startActivity(intent)
        }

        bottomNav?.menu?.findItem(R.id.nav_placeholder)?.isEnabled = false
        bottomNav?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_templates -> true
                R.id.nav_docs -> true
                else -> false
            }
        }

        fabHome?.setOnClickListener {
            bottomNav?.selectedItemId = R.id.nav_placeholder
        }
    }

    private fun setupRecyclerView() {
        val rvDocuments = findViewById<RecyclerView>(R.id.rvDocuments)
        rvDocuments.layoutManager = LinearLayoutManager(this)

        // Start with an empty list
        documentAdapter = DocumentAdapter(emptyList<Document>())
        rvDocuments.adapter = documentAdapter
    }

    private fun loadUserDocuments() {
        // Dummy data to ensure the list works visually
        val mySavedDocs = listOf(
            Document("My Resume", "Sep 25, 2025", true),
            Document("Cover Letter - Software Engineer", "Sep 24, 2025", false)
        )
        documentAdapter.updateData(mySavedDocs)
    }
}