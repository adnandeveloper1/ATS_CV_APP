package com.example.resumebuilder.Activities

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.resume_builder.Dataclass.ResumeRepository
import com.example.resumebuilder.Adapter.ResumeEducationAdapter
import com.example.resumebuilder.Adapter.ResumeExperienceAdapter
import com.example.resumebuilder.R
import com.google.android.material.button.MaterialButton
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ModernActivity : AppCompatActivity() {

    private var experienceAdapter: ResumeExperienceAdapter? = null
    private var educationAdapter: ResumeEducationAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_modern)

        val rootView = findViewById<View>(R.id.main) ?: window.decorView
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Action bar setup
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnEdit = findViewById<MaterialButton>(R.id.btnEdit)
        val btnDownload = findViewById<MaterialButton>(R.id.btnDownload)
        val cvScrollView = findViewById<ScrollView>(R.id.main_scroll_view)

        btnBack?.setOnClickListener { finish() }
        btnEdit?.setOnClickListener { finish() }
        btnDownload?.setOnClickListener {
            if (cvScrollView != null) {
                saveCvAsPdf(cvScrollView)
            } else {
                Toast.makeText(this, "Error finding CV layout.", Toast.LENGTH_SHORT).show()
            }
        }

        // 1. Populate Personal Details
        findViewById<TextView>(R.id.username)?.text = ResumeRepository.name.ifEmpty { "Philip Franklin" }
        findViewById<TextView>(R.id.address)?.text = ResumeRepository.address.ifEmpty { "Virginia Beach, VA" }
        findViewById<TextView>(R.id.email)?.text = ResumeRepository.email.ifEmpty { "philip@email.com" }
        findViewById<TextView>(R.id.phonenumber)?.text = ResumeRepository.phone.ifEmpty { "(088) 000 0000" }
        findViewById<TextView>(R.id.introduction)?.text = ResumeRepository.summary.ifEmpty { "Professional summary text goes here..." }

        // 2. Populate Experience List
        val rvExperience = findViewById<RecyclerView>(R.id.rv_experience)
        if (rvExperience != null) {
            rvExperience.layoutManager = LinearLayoutManager(this)
            experienceAdapter = ResumeExperienceAdapter(ResumeRepository.experienceList)
            rvExperience.adapter = experienceAdapter
            rvExperience.isNestedScrollingEnabled = false
        }

        // 3. Populate Education List
        val rvEducation = findViewById<RecyclerView>(R.id.rv_education)
        if (rvEducation != null) {
            rvEducation.layoutManager = LinearLayoutManager(this)
            educationAdapter = ResumeEducationAdapter(ResumeRepository.educationList)
            rvEducation.adapter = educationAdapter
            rvEducation.isNestedScrollingEnabled = false
        }

        // 4. Populate Bulleted Skills List (Hides section if empty)
        setupSkillsSection()
    }

    override fun onResume() {
        super.onResume()
        // Re-evaluate skills formatting and refresh RecyclerView lists
        setupSkillsSection()
        experienceAdapter?.notifyDataSetChanged()
        educationAdapter?.notifyDataSetChanged()
    }

    private fun setupSkillsSection() {
        val tvSkillsFormatted = findViewById<TextView>(R.id.tvSkillsFormatted)
        val validSkills = ResumeRepository.skillsList.filter { it.isNotBlank() }

        if (tvSkillsFormatted != null) {
            if (validSkills.isNotEmpty()) {
                tvSkillsFormatted.visibility = View.VISIBLE
                tvSkillsFormatted.text = validSkills.joinToString("\n") { "• $it" }
            } else {
                // Completely hide text view when list is empty to prevent empty bullet points
                tvSkillsFormatted.visibility = View.GONE
            }
        }
    }

    private fun saveCvAsPdf(scrollView: ScrollView) {
        val childView = scrollView.getChildAt(0) ?: run {
            Toast.makeText(this, "Layout is empty.", Toast.LENGTH_SHORT).show()
            return
        }

        val totalHeight = childView.height
        val totalWidth = childView.width

        if (totalHeight <= 0 || totalWidth <= 0) {
            Toast.makeText(this, "Unable to generate PDF. Layout not rendered yet.", Toast.LENGTH_SHORT).show()
            return
        }

        val bitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        scrollView.draw(canvas)

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(totalWidth, totalHeight, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        page.canvas.drawBitmap(bitmap, 0f, 0f, null)
        pdfDocument.finishPage(page)

        val fileName = "Modern_Resume_${System.currentTimeMillis()}.pdf"
        var outputStream: OutputStream? = null
        var savedUri: android.net.Uri? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }
                val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                savedUri = uri
                if (uri != null) {
                    outputStream = contentResolver.openOutputStream(uri)
                }
            } else {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) downloadsDir.mkdirs()
                val file = File(downloadsDir, fileName)
                outputStream = FileOutputStream(file)
                savedUri = android.net.Uri.fromFile(file)
            }

            if (outputStream != null) {
                pdfDocument.writeTo(outputStream)
                outputStream.flush()
                outputStream.close()
                outputStream = null

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && savedUri != null) {
                    val updateValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.IS_PENDING, 0)
                    }
                    contentResolver.update(savedUri, updateValues, null, null)
                } else if (savedUri != null) {
                    val intent = android.content.Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                    intent.data = savedUri
                    sendBroadcast(intent)
                }

                Toast.makeText(this, "PDF saved to Downloads folder!", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            try { outputStream?.close() } catch (ignored: Exception) {}
            pdfDocument.close()
        }
    }
}