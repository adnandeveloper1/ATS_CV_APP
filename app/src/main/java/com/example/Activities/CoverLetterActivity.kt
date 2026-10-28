package com.example.Activities

import android.app.Dialog
import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.resumebuilder.Activities.CoverLetterPagerAdapter
import com.example.resumebuilder.Adapters.DetailsFragment
import com.example.resumebuilder.Adapters.TemplateFragment
import com.example.resumebuilder.Fragments.PreviewFragment
import com.example.resumebuilder.R
import com.example.resumebuilder.DocumentManager
import com.example.CoverLetterViewModel
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson

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

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cover_letter)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeBars = insets.getInsets(WindowInsetsCompat.Type.ime())
            val bottomPadding = Math.max(systemBars.bottom, imeBars.bottom)

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                bottomPadding
            )
            insets
        }

        viewModel = ViewModelProvider(this)[CoverLetterViewModel::class.java]

        viewPager = findViewById(R.id.viewPager)
        btnBack = findViewById(R.id.btnBack)
        btnNext = findViewById(R.id.btnNext)

        tvStep1Num = findViewById(R.id.tvStep1Num)
        tvStep1Label = findViewById(R.id.tvStep1Label)
        tvStep2Num = findViewById(R.id.tvStep2Num)
        tvStep2Label = findViewById(R.id.tvStep2Label)
        tvStep3Num = findViewById(R.id.tvStep3Num)
        tvStep3Label = findViewById(R.id.tvStep3Label)

        pagerAdapter = CoverLetterPagerAdapter(this)
        viewPager.adapter = pagerAdapter
        viewPager.isUserInputEnabled = false

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateStepIndicator(position)
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

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun handleNextClick() {
        when (viewPager.currentItem) {
            0 -> {
                val detailsFragment = pagerAdapter.getFragment(0) as? DetailsFragment
                val jobDetails = detailsFragment?.validateAndGetDetails()

                if (jobDetails != null) {
                    viewModel.jobDetails = jobDetails
                    saveCoverLetterUserDetails()
                    viewPager.currentItem = 1
                }
            }

            1 -> {
                val templateFragment = pagerAdapter.getFragment(1) as? TemplateFragment
                val selected = templateFragment?.getSelectedTemplate()

                if (selected == null) {
                    Toast.makeText(this, "Please select a template", Toast.LENGTH_SHORT).show()
                    return
                }

                viewModel.selectedTemplate = selected
                viewPager.currentItem = 2

                viewPager.post {
                    val previewFragment = pagerAdapter.getFragment(2) as? PreviewFragment
                    previewFragment?.refreshPreview()
                }
            }

            2 -> {
                downloadCoverLetter()
            }
        }
    }

    private fun saveCoverLetterUserDetails() {
        val details = viewModel.jobDetails ?: return
        val prefs = getSharedPreferences("CoverLetterData", MODE_PRIVATE).edit()

        prefs.putString("name", details.userName)
        prefs.putString("email", details.userEmail)
        prefs.putString("phone", details.contactNum)
        prefs.putString("jobTitle", details.jobTitle)
        prefs.putString("companyName", details.companyName)
        prefs.putString("recipientName", details.recipientName)
        prefs.putString("notes", details.notes)
        prefs.apply()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun downloadCoverLetter() {
        val previewFragment = pagerAdapter.getFragment(2) as? PreviewFragment
        val templateView = previewFragment?.getTemplateView()

        if (templateView == null) {
            Toast.makeText(this, "Template not ready", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(
                templateView.width,
                templateView.height,
                1
            ).create()

            val page = pdfDocument.startPage(pageInfo)
            templateView.draw(page.canvas)
            pdfDocument.finishPage(page)

            val fileName = "CoverLetter_${System.currentTimeMillis()}.pdf"

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val uri = contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                contentValues
            )

            uri?.let {
                contentResolver.openOutputStream(it)?.use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
            }

            pdfDocument.close()
            saveCoverLetterUserDetails()

            val dataJsonPayload = if (viewModel.jobDetails != null) {
                Gson().toJson(viewModel.jobDetails)
            } else {
                ""
            }

            // Robust template matching by checking both string name and template ID/position
            val rawName = viewModel.selectedTemplate?.name ?: ""
            val templateId = viewModel.selectedTemplate?.id ?: -1

            val templateType = when {
                rawName.contains("creative", ignoreCase = true) || rawName.contains("modern", ignoreCase = true) || templateId == 2 -> "Creative Cover Letter"
                rawName.contains("professional", ignoreCase = true) || templateId == 3 -> "Professional Cover Letter"
                rawName.contains("minimal", ignoreCase = true) || templateId == 1 -> "Minimal Cover Letter"
                else -> if (rawName.isNotEmpty()) rawName else "Creative Cover Letter"
            }

            val letterTitle = viewModel.jobDetails?.jobTitle ?: "Job"

            DocumentManager.saveDocument(
                context = this,
                title = "$letterTitle Cover Letter",
                isResume = false,
                templateType = templateType,
                dataJson = dataJsonPayload
            )

            showDownloadSuccessDialog()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Download failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showDownloadSuccessDialog() {
        val dialog = Dialog(this, R.style.CustomDialogTheme)
        dialog.setContentView(R.layout.dialog_customsucesful)

        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                (resources.displayMetrics.widthPixels * 0.88).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val btnOk = dialog.findViewById<MaterialButton>(R.id.btnOk)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvDialogTitle)
        val tvMessage = dialog.findViewById<TextView>(R.id.tvDialogMessage)

        tvTitle.text = "Download Successful!"
        tvMessage.text = "Your cover letter has been downloaded and saved to My Documents."

        btnOk.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }

    private fun updateStepIndicator(position: Int) {
        val activeBg = R.drawable.bg_dot_active
        val inactiveBg = R.drawable.bg_dot_inactive
        val activeColor = Color.parseColor("#2563EB")
        val inactiveColor = Color.parseColor("#94A3B8")

        tvStep1Num.setBackgroundResource(if (position >= 0) activeBg else inactiveBg)
        tvStep1Num.setTextColor(if (position >= 0) Color.WHITE else inactiveColor)
        tvStep1Label.setTextColor(if (position >= 0) activeColor else inactiveColor)

        tvStep2Num.setBackgroundResource(if (position >= 1) activeBg else inactiveBg)
        tvStep2Num.setTextColor(if (position >= 1) Color.WHITE else inactiveColor)
        tvStep2Label.setTextColor(if (position >= 1) activeColor else inactiveColor)

        tvStep3Num.setBackgroundResource(if (position >= 2) activeBg else inactiveBg)
        tvStep3Num.setTextColor(if (position >= 2) Color.WHITE else inactiveColor)
        tvStep3Label.setTextColor(if (position >= 2) activeColor else inactiveColor)
    }
}