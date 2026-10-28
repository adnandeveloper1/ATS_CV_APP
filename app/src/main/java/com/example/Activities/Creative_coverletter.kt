package com.example.Activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.resumebuilder.Adapters.JobDetails
import com.example.resumebuilder.DocumentManager
import com.example.resumebuilder.R
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson

class Creative_coverletter : AppCompatActivity() {

    private var currentJobDetails: JobDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_creative_coverletter)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dataJson = intent.getStringExtra("dataJson") ?: ""
        if (dataJson.isNotEmpty()) {
            try {
                currentJobDetails = Gson().fromJson(dataJson, JobDetails::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val name          = currentJobDetails?.userName ?: intent.getStringExtra("name") ?: ""
        val email         = currentJobDetails?.userEmail ?: intent.getStringExtra("email") ?: ""
        val phone         = currentJobDetails?.contactNum ?: intent.getStringExtra("phone") ?: ""
        val jobTitle      = currentJobDetails?.jobTitle ?: intent.getStringExtra("jobTitle") ?: ""
        val companyName   = currentJobDetails?.companyName ?: intent.getStringExtra("companyName") ?: ""
        val recipientName = currentJobDetails?.recipientName ?: intent.getStringExtra("recipientName") ?: ""
        val notes         = currentJobDetails?.notes ?: intent.getStringExtra("notes") ?: ""

        if (currentJobDetails == null) {
            currentJobDetails = JobDetails(name, email, phone, jobTitle, companyName, recipientName, notes)
        }

        bindData(name, email, phone, jobTitle, companyName, recipientName, notes)

        val btnDownload = findViewById<MaterialButton>(R.id.btnDownload)
        btnDownload?.setOnClickListener {
            val docTitle = if (name.isNotEmpty()) "$name's Creative Cover Letter" else "Creative Cover Letter"
            val jsonPayload = Gson().toJson(currentJobDetails)

            DocumentManager.saveDocument(
                context = this,
                title = docTitle,
                isResume = false,
                templateType = "Creative Cover Letter",
                dataJson = jsonPayload
            )

            showSuccessDialog()
        }
    }

    private fun showSuccessDialog() {
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
        tvMessage.text = "Creative Cover Letter has been saved to My Documents."

        btnOk.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }

    private fun bindData(
        name: String,
        email: String,
        phone: String,
        jobTitle: String,
        companyName: String,
        recipientName: String,
        notes: String
    ) {
        findViewById<TextView>(R.id.tvName)?.text = name.ifEmpty { "YOUR NAME" }
        findViewById<TextView>(R.id.tvJobTitle)?.text = jobTitle.ifEmpty { "JOB TITLE" }
        findViewById<TextView>(R.id.tvEmail)?.text = email.ifEmpty { "email@gmail.com" }
        findViewById<TextView>(R.id.tvPhone)?.text = phone.ifEmpty { "(000) 000-0000" }
        findViewById<TextView>(R.id.tvCompanyName)?.text = companyName.ifEmpty { "Company Name" }

        val tvToHiringManager = findViewById<TextView>(R.id.tvToHiringManager)
        val tvGreeting        = findViewById<TextView>(R.id.tvGreeting)

        if (recipientName.isNotEmpty()) {
            tvToHiringManager?.text = "TO ${recipientName.uppercase()}"
            tvGreeting?.text = "Dear $recipientName,"
        } else {
            tvToHiringManager?.text = "TO HIRING MANAGER"
            tvGreeting?.text = "Dear Hiring Manager,"
        }

        val tvBody = findViewById<TextView>(R.id.tvBody)
        tvBody?.text = if (notes.isNotEmpty()) {
            notes
        } else {
            "I am excited to apply for the $jobTitle position at $companyName. " +
                    "My experience in managing operations and communication will help ensure projects run efficiently."
        }

        findViewById<TextView>(R.id.tvClosing)?.text = "Sincerely,"
        findViewById<TextView>(R.id.tvSignature)?.text = name.ifEmpty { "Your Name" }
    }
}