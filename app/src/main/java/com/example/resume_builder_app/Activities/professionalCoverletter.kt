package com.example.resume_builder_app.Activities

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.resume_builder_app.R

class professionalCoverletter : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Get selected template type
        val templateType = intent.getStringExtra("templateType") ?: "Professional"

        // 2. Load correct layout according to selected template
        when (templateType) {
            "Professional" -> setContentView(R.layout.activity_professional_coverletter)
            "Modern"       -> setContentView(R.layout.activity_creative_coverletter)
            "Minimal"      -> setContentView(R.layout.activity_minimal_coverletter)
            else           -> setContentView(R.layout.activity_professional_coverletter)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 3. Get data from Intent
        val name          = intent.getStringExtra("name") ?: ""
        val email         = intent.getStringExtra("email") ?: ""
        val phone         = intent.getStringExtra("phone") ?: ""
        val jobTitle      = intent.getStringExtra("jobTitle") ?: ""
        val companyName   = intent.getStringExtra("companyName") ?: ""
        val recipientName = intent.getStringExtra("recipientName") ?: ""
        val notes         = intent.getStringExtra("notes") ?: ""

        // 4. Bind data to views according to current layout
        bindData(name, email, phone, jobTitle, companyName, recipientName, notes, templateType)
    }

    private fun bindData(
        name: String,
        email: String,
        phone: String,
        jobTitle: String,
        companyName: String,
        recipientName: String,
        notes: String,
        templateType: String
    ) {
        // Common fields across templates
        findViewById<TextView>(R.id.tvName)?.text = name.uppercase()
        findViewById<TextView>(R.id.tvJobTitle)?.text = jobTitle.uppercase()
        findViewById<TextView>(R.id.tvEmail)?.text = email.ifEmpty { "Your Gmail" }
        findViewById<TextView>(R.id.tvPhone)?.text = phone.ifEmpty { "Tell Number" }

        val greetingStr = if (recipientName.isNotEmpty()) "Dear $recipientName," else "Dear Hiring Manager,"
        findViewById<TextView>(R.id.tvGreeting)?.text = greetingStr

        if (templateType == "Minimal") {
            // Intro Paragraph for Minimal
            val defaultIntro = "It is with great interest that I submit my application for the position of $jobTitle at $companyName. " +
                    "I am confident that my skills and experience make me a strong fit for this role."
            findViewById<TextView>(R.id.tvIntro)?.text = defaultIntro

            // WHY ME SECTION: Binds user input from etAdditionalNotes directly here
            val defaultWhyMe = "• Strong organizational and communication skills\n" +
                    "• Proven ability to work under pressure and meet deadlines\n" +
                    "• Experience in managing office operations efficiently\n" +
                    "• Passionate about contributing to team success"

            findViewById<TextView>(R.id.tvWhyMe)?.text = if (notes.isNotEmpty()) notes else defaultWhyMe

            // Why Company Section
            val companyHeading = if (companyName.isNotEmpty()) "Why $companyName?" else "Why (Company Name)?"
            findViewById<TextView>(R.id.companyname)?.text = companyHeading
            findViewById<TextView>(R.id.tvWhyCompany)?.text = "I am particularly interested in $companyName because of its reputation and values."

            findViewById<TextView>(R.id.tvClosing)?.text = "Yours Sincerely,"
            findViewById<TextView>(R.id.tvSignature)?.text = name

        } else {
            // Professional & Creative templates
            val toHiringManagerStr = if (recipientName.isNotEmpty()) "TO ${recipientName.uppercase()}" else "TO HIRING MANAGER"
            findViewById<TextView>(R.id.tvToHiringManager)?.text = toHiringManagerStr
            findViewById<TextView>(R.id.tvCompanyName)?.text = companyName

            // Body text for Professional/Creative
            val defaultBody = "I am excited to apply for the $jobTitle position at $companyName. " +
                    "My experience in office management, organization, and communication will be highly valuable to your team."
            findViewById<TextView>(R.id.tvBody)?.text = if (notes.isNotEmpty()) notes else defaultBody

            findViewById<TextView>(R.id.tvClosing)?.text = "Sincerely,"
            findViewById<TextView>(R.id.tvSignature)?.text = name
        }
    }
}