package com.example.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.resume_builder.Dataclass.ResumeRepository
import com.example.resumebuilder.DocumentAdapter
import com.example.resumebuilder.DocumentManager
import com.example.resumebuilder.R
import com.example.resumebuilder.SwipeToDeleteCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {

    private lateinit var documentAdapter: DocumentAdapter
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var rvDocuments: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val btnSettings = findViewById<ImageView>(R.id.btnSettings)
        btnSettings?.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        val tvViewAll = findViewById<TextView>(R.id.tvViewAll)
        tvViewAll?.setOnClickListener {
            startActivity(Intent(this, MyDocsActivity::class.java))
        }

        setupButtons()
        setupRecyclerView()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserDocuments()
        if (::bottomNav.isInitialized) {
            bottomNav.selectedItemId = R.id.nav_placeholder
        }
    }

    private fun setupButtons() {
        bottomNav = findViewById(R.id.bottomNavigationView)
        val fabHome = findViewById<FloatingActionButton>(R.id.fabHome)

        bottomNav.menu.findItem(R.id.nav_placeholder)?.isEnabled = false
        bottomNav.selectedItemId = R.id.nav_placeholder

        // 1. Create Resume Click: Static repository ko clear karke fresh state shuru karein
        val cardCreateResume = findViewById<ConstraintLayout>(R.id.cardCreateResume)
        cardCreateResume?.setOnClickListener {
            ResumeRepository.clearAll()
            startActivity(Intent(this, ResumeActivity::class.java))
        }

        // 2. Create Cover Letter Click: Fresh state initialization
        val cardCreateCoverLetter = findViewById<ConstraintLayout>(R.id.cardCreateCoverLetter)
        cardCreateCoverLetter?.setOnClickListener {
            ResumeRepository.clearAll()
            startActivity(Intent(this, CoverLetterActivity::class.java))
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_templates -> {
                    startActivity(Intent(this, TemplatesActivity::class.java))
                    true
                }
                R.id.nav_docs -> {
                    startActivity(Intent(this, MyDocsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        fabHome?.setOnClickListener {
            bottomNav.selectedItemId = R.id.nav_placeholder
        }
    }

    private fun setupRecyclerView() {
        rvDocuments = findViewById(R.id.rvDocuments)
        rvDocuments.layoutManager = LinearLayoutManager(this)

        documentAdapter = DocumentAdapter(emptyList())
        rvDocuments.adapter = documentAdapter

        // Custom Animated Swipe Callback
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val savedDocs = DocumentManager.getDocuments(this@HomeActivity)

                if (position in savedDocs.indices) {
                    val targetDoc = savedDocs[position]
                    DocumentManager.deleteDocument(this@HomeActivity, targetDoc)

                    loadUserDocuments()
                    Toast.makeText(this@HomeActivity, "Document Deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rvDocuments)
    }

    private fun loadUserDocuments() {
        val savedDocs = DocumentManager.getDocuments(this)
        documentAdapter.updateData(savedDocs)
    }
}