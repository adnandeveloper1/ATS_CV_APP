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

class professionalCoverletter : AppCompatActivity() {

    private var currentJobDetails: JobDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_professional_coverletter)

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
            val docTitle = if (name.isNotEmpty()) "$name's Professional Cover Letter" else "Professional Cover Letter"
            val jsonPayload = Gson().toJson(currentJobDetails)

            DocumentManager.saveDocument(
                context = this,
                title = docTitle,
                isResume = false,
                templateType = "Professional Cover Letter",
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
        tvMessage.text = "Professional Cover Letter has been saved to My Documents."

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
        val tvName            = findViewById<TextView>(R.id.tvName)
        val tvJobTitle        = findViewById<TextView>(R.id.tvJobTitle)
        val tvEmail           = findViewById<TextView>(R.id.tvEmail)
        val tvPhone           = findViewById<TextView>(R.id.tvPhone)
        val tvToHiringManager = findViewById<TextView>(R.id.tvToHiringManager)
        val tvCompanyName     = findViewById<TextView>(R.id.tvCompanyName)
        val tvGreeting        = findViewById<TextView>(R.id.tvGreeting)
        val tvBody            = findViewById<TextView>(R.id.tvBody)
        val tvClosing         = findViewById<TextView>(R.id.tvClosing)
        val tvSignature       = findViewById<TextView>(R.id.tvSignature)

        tvName?.text = name.ifEmpty { "YOUR NAME" }.uppercase()
        tvJobTitle?.text = jobTitle.ifEmpty { "JOB TITLE" }.uppercase()
        tvEmail?.text = email.ifEmpty { "email@gmail.com" }
        tvPhone?.text = phone.ifEmpty { "(000) 000-0000" }
        tvCompanyName?.text = companyName.ifEmpty { "Company Name" }

        if (recipientName.isNotEmpty()) {
            tvToHiringManager?.text = "TO ${recipientName.uppercase()}"
            tvGreeting?.text = "Dear $recipientName,"
        } else {
            tvToHiringManager?.text = "TO HIRING MANAGER"
            tvGreeting?.text = "Dear Hiring Manager,"
        }

        val companyText = if (companyName.isNotEmpty()) companyName else "your company"
        val jobText = if (jobTitle.isNotEmpty()) jobTitle else "this role"

        val bodyNotes = if (notes.isNotEmpty()) {
            notes
        } else {
            "• Strong organizational and communication skills\n" +
                    "• Proven ability to work under pressure and meet deadlines\n" +
                    "• Experience in managing operations efficiently\n" +
                    "• Passionate about contributing to team success"
        }

        val defaultBody = "It is with great interest that I submit my application for the position of $jobText at $companyText. " +
                "I am confident that my skills and experience make me a strong fit for this role.\n\n$bodyNotes"

        tvBody?.text = defaultBody
        tvClosing?.text = "Sincerely,"
        tvSignature?.text = name.ifEmpty { "Your Name" }
    }
}