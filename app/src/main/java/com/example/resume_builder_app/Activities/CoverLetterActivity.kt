package com.example.resume_builder_app.Activities

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.resume_builder_app.Adapters.DetailsFragment
import com.example.resume_builder_app.Adapters.TemplateFragment
import com.example.resume_builder_app.Fragments.PreviewFragment
import com.example.resume_builder_app.R
import com.example.resume_builder_app.viewmodel.CoverLetterViewModel
import com.google.android.material.button.MaterialButton

class CoverLetterActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var btnBack: ImageView
    private lateinit var btnNext: MaterialButton

    private lateinit var tvStep1Num: TextView
    private lateinit var tvStep1Label: TextView
    private lateinit var tvStep2Num: TextView
    private lateinit var tvStep2Label: TextView
    private lateinit var tvStep3Num: TextView
    private lateinit var tvStep3Label: TextView

    private lateinit var viewModel: CoverLetterViewModel
    private lateinit var pagerAdapter: CoverLetterPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cover_letter)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        // ViewModel
        viewModel = ViewModelProvider(this)[CoverLetterViewModel::class.java]

        // Find views
        viewPager = findViewById(R.id.viewPager)
        btnBack = findViewById(R.id.btnBack)
        btnNext = findViewById(R.id.btnNext)

        tvStep1Num = findViewById(R.id.tvStep1Num)
        tvStep1Label = findViewById(R.id.tvStep1Label)
        tvStep2Num = findViewById(R.id.tvStep2Num)
        tvStep2Label = findViewById(R.id.tvStep2Label)
        tvStep3Num = findViewById(R.id.tvStep3Num)
        tvStep3Label = findViewById(R.id.tvStep3Label)

        // Setup ViewPager
        pagerAdapter = CoverLetterPagerAdapter(this)
        viewPager.adapter = pagerAdapter
        viewPager.isUserInputEnabled = false

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateStepIndicator(position)

                // Change button text on last step
                btnNext.text = if (position == 2) "Download" else "Next"
            }
        })

        btnBack.setOnClickListener {
            if (viewPager.currentItem > 0) {
                viewPager.currentItem -= 1
            } else {
                finish()
            }
        }

        btnNext.setOnClickListener {
            handleNextClick()
        }
    }

    private fun handleNextClick() {
        when (viewPager.currentItem) {

            // Step 0 → Details
            0 -> {
                val detailsFragment = pagerAdapter.getFragment(0) as? DetailsFragment
                val jobDetails = detailsFragment?.validateAndGetDetails()

                if (jobDetails != null) {
                    viewModel.jobDetails = jobDetails
                    viewPager.currentItem = 1
                }
            }

            1 -> { // Template step
                val templateFragment = pagerAdapter.getFragment(1) as? TemplateFragment
                val selected = templateFragment?.getSelectedTemplate()

                if (selected == null) {
                    Toast.makeText(this, "Please select a template", Toast.LENGTH_SHORT).show()
                    return
                }

                viewModel.selectedTemplate = selected
                viewPager.currentItem = 2

                // Preview ko force refresh karo
                viewPager.post {
                    val previewFragment = pagerAdapter.getFragment(2) as? PreviewFragment
                    previewFragment?.refreshPreview()
                }
            }

            // Step 2 → Download
            2 -> {
                downloadCoverLetter()
            }
        }
    }

    private fun downloadCoverLetter() {
        val previewFragment = pagerAdapter.getFragment(2) as? PreviewFragment
        val templateView = previewFragment?.getTemplateView()

        if (templateView == null) {
            Toast.makeText(this, "Template not ready", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val pdfDocument = android.graphics.pdf.PdfDocument()
            val pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(
                templateView.width,
                templateView.height,
                1
            ).create()

            val page = pdfDocument.startPage(pageInfo)
            templateView.draw(page.canvas)
            pdfDocument.finishPage(page)

            val fileName = "CoverLetter_${System.currentTimeMillis()}.pdf"

            val contentValues = android.content.ContentValues().apply {
                put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, android.os.Environment.DIRECTORY_DOWNLOADS)
            }

            val uri = contentResolver.insert(
                android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                contentValues
            )

            uri?.let {
                contentResolver.openOutputStream(it)?.use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
            }

            pdfDocument.close()

            Toast.makeText(this, "Cover Letter downloaded successfully!", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Download failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateStepIndicator(position: Int) {
        val activeBg = R.drawable.bg_dot_active
        val inactiveBg = R.drawable.bg_dot_inactive
        val activeColor = Color.parseColor("#2563EB")
        val inactiveColor = Color.parseColor("#94A3B8")

        // Step 1
        tvStep1Num.setBackgroundResource(if (position >= 0) activeBg else inactiveBg)
        tvStep1Num.setTextColor(if (position >= 0) Color.WHITE else inactiveColor)
        tvStep1Label.setTextColor(if (position >= 0) activeColor else inactiveColor)

        // Step 2
        tvStep2Num.setBackgroundResource(if (position >= 1) activeBg else inactiveBg)
        tvStep2Num.setTextColor(if (position >= 1) Color.WHITE else inactiveColor)
        tvStep2Label.setTextColor(if (position >= 1) activeColor else inactiveColor)

        // Step 3
        tvStep3Num.setBackgroundResource(if (position >= 2) activeBg else inactiveBg)
        tvStep3Num.setTextColor(if (position >= 2) Color.WHITE else inactiveColor)
        tvStep3Label.setTextColor(if (position >= 2) activeColor else inactiveColor)
    }
}