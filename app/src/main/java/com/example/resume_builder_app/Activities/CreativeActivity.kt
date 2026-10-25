package com.example.resume_builder_app.Activities

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
import com.example.resume_builder_app.Adapter.ResumeEducationAdapter
import com.example.resume_builder_app.Adapter.ResumeExperienceAdapter
import com.example.resume_builder_app.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class CreativeActivity : AppCompatActivity() {

    private var experienceAdapter: ResumeExperienceAdapter? = null
    private var educationAdapter: ResumeEducationAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_creative)

        val rootView = findViewById<View>(R.id.main) ?: window.decorView
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

        // 1. Header & Contact Details
        val phone = ResumeRepository.phone.ifEmpty { "+1 555-555-890" }
        val email = ResumeRepository.email.ifEmpty { "johnsmith@email.com" }
        val address = ResumeRepository.address.ifEmpty { "San Francisco, CA" }

        findViewById<TextView>(R.id.username)?.text = ResumeRepository.name.ifEmpty { "JOHN SMITH" }
        findViewById<TextView>(R.id.role)?.text = ResumeRepository.role.ifEmpty { "GRAPHIC DESIGNER" }
        findViewById<TextView>(R.id.tvContactInfo)?.text = "$phone  |  $email  |  $address"
        findViewById<TextView>(R.id.introduction)?.text = ResumeRepository.summary.ifEmpty { "Creative summary text..." }

        // 2. Dynamic Skills (Hides section if empty)
        setupSkillsSection()

        // 3. Work Experience RecyclerView Setup
        val rvExperience = findViewById<RecyclerView>(R.id.rv_experience)
        if (rvExperience != null) {
            rvExperience.layoutManager = LinearLayoutManager(this)
            experienceAdapter = ResumeExperienceAdapter(ResumeRepository.experienceList)
            rvExperience.adapter = experienceAdapter
            rvExperience.isNestedScrollingEnabled = false
        }

        // 4. Education RecyclerView Setup
        val rvEducation = findViewById<RecyclerView>(R.id.rv_education)
        if (rvEducation != null) {
            rvEducation.layoutManager = LinearLayoutManager(this)
            educationAdapter = ResumeEducationAdapter(ResumeRepository.educationList)
            rvEducation.adapter = educationAdapter
            rvEducation.isNestedScrollingEnabled = false
        }
    }

    override fun onResume() {
        super.onResume()
        // Re-evaluate skills section and notify adapters when returning from edit screen
        setupSkillsSection()
        experienceAdapter?.notifyDataSetChanged()
        educationAdapter?.notifyDataSetChanged()
    }

    private fun setupSkillsSection() {
        val chipGroupSkills = findViewById<ChipGroup>(R.id.chipGroupSkills)
        val validSkills = ResumeRepository.skillsList.filter { it.isNotBlank() }

        if (chipGroupSkills != null) {
            chipGroupSkills.removeAllViews()

            if (validSkills.isNotEmpty()) {
                chipGroupSkills.visibility = View.VISIBLE

                validSkills.forEach { skill ->
                    val chip = Chip(this).apply {
                        text = skill
                        isCloseIconVisible = false
                        isClickable = false
                        setChipBackgroundColorResource(android.R.color.transparent)
                        setChipStrokeColorResource(android.R.color.darker_gray)
                        chipStrokeWidth = 2f
                        setTextColor(resources.getColor(android.R.color.black, null))
                        textSize = 11f
                    }
                    chipGroupSkills.addView(chip)
                }
            } else {
                // Hide chip container completely when empty to avoid drawing empty circle outlines
                chipGroupSkills.visibility = View.GONE
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

        val fileName = "Creative_Resume_${System.currentTimeMillis()}.pdf"
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