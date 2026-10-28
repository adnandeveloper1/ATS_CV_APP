package com.example.Activities

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.resumebuilder.Document
import com.example.resumebuilder.DocumentAdapter
import com.example.resumebuilder.DocumentManager
import com.example.resumebuilder.R
import com.example.resumebuilder.SwipeToDeleteCallback
import com.google.android.material.tabs.TabLayout

class MyDocsActivity : AppCompatActivity() {

    private lateinit var documentAdapter: DocumentAdapter
    private lateinit var rvMyDocuments: RecyclerView
    private lateinit var tabLayout: TabLayout

    private var displayedDocuments: List<Document> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_docs)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        rvMyDocuments = findViewById(R.id.rvMyDocuments)
        tabLayout = findViewById(R.id.tabLayout)

        setupRecyclerView()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    filterDocumentsAndShow(it.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        loadAllDocumentsFromDatabase()
    }

    private fun setupRecyclerView() {
        rvMyDocuments.layoutManager = LinearLayoutManager(this)
        documentAdapter = DocumentAdapter(emptyList())
        rvMyDocuments.adapter = documentAdapter

        // Custom Animated Swipe Callback
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (position in displayedDocuments.indices) {
                    val docToDelete = displayedDocuments[position]

                    DocumentManager.deleteDocument(this@MyDocsActivity, docToDelete)
                    loadAllDocumentsFromDatabase()

                    Toast.makeText(this@MyDocsActivity, "Document Deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rvMyDocuments)
    }

    private fun loadAllDocumentsFromDatabase() {
        filterDocumentsAndShow(tabLayout.selectedTabPosition)
    }

    private fun filterDocumentsAndShow(tabPosition: Int) {
        val allDocs = DocumentManager.getDocuments(this)

        displayedDocuments = if (tabPosition == 0) {
            allDocs.filter { it.isResume }
        } else {
            allDocs.filter { !it.isResume }
        }

        documentAdapter.updateData(displayedDocuments)
    }
}