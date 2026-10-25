package com.example.resume_builder_app.Activities

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.resume_builder_app.R

class Minimal_coverletter : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_minimal_coverletter)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Receive data from Intent
        val name          = intent.getStringExtra("name") ?: ""
        val email         = intent.getStringExtra("email") ?: ""
        val phone         = intent.getStringExtra("phone") ?: ""
        val jobTitle      = intent.getStringExtra("jobTitle") ?: ""
        val companyName   = intent.getStringExtra("companyName") ?: ""
        val recipientName = intent.getStringExtra("recipientName") ?: ""
        val notes         = intent.getStringExtra("notes") ?: ""

        // Bind data to views
        bindData(name, email, phone, jobTitle, companyName, recipientName, notes)
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
        // Name & Job Title
        val displayName = if (name.isNotEmpty()) name.uppercase() else "YOUR NAME"
        val displayJobTitle = if (jobTitle.isNotEmpty()) jobTitle.uppercase() else "TARGET JOB TITLE"

        findViewById<TextView>(R.id.tvName)?.text = displayName
        findViewById<TextView>(R.id.tvJobTitle)?.text = displayJobTitle

        // Contact Info
        findViewById<TextView>(R.id.tvEmail)?.text = email.ifEmpty { "Your Email" }
        findViewById<TextView>(R.id.tvPhone)?.text = phone.ifEmpty { "Contact Number" }

        // Greeting
        val greeting = if (recipientName.isNotEmpty()) {
            "Dear $recipientName,"
        } else {
            "Dear Hiring Manager,"
        }
        findViewById<TextView>(R.id.tvGreeting)?.text = greeting

        // Intro paragraph
        val companyText = if (companyName.isNotEmpty()) companyName else "your company"
        val jobText = if (jobTitle.isNotEmpty()) jobTitle else "this role"

        val intro = "It is with great interest that I submit my application for the position of $jobText at $companyText. " +
                "I am confident that my skills and experience make me a strong fit for this role."
        findViewById<TextView>(R.id.tvIntro)?.text = intro

        // Why Me Section (Uses notes from etAdditionalNotes, or fallback points if empty)
        val defaultWhyMe = "• Strong organizational and communication skills\n" +
                "• Proven ability to work under pressure and meet deadlines\n" +
                "• Experience in managing operations efficiently\n" +
                "• Passionate about contributing to team success"

        findViewById<TextView>(R.id.tvWhyMe)?.text = if (notes.isNotEmpty()) notes else defaultWhyMe

        // Why Company Heading & Text
        val companyHeading = if (companyName.isNotEmpty()) "Why $companyName?" else "Why (Company Name)?"
        findViewById<TextView>(R.id.companyname)?.text = companyHeading

        val whyCompanyText = if (companyName.isNotEmpty()) {
            "I am particularly interested in $companyName because of its commitment to innovation, growth, and excellence. I would love to contribute my skills toward achieving your goals."
        } else {
            "I am particularly interested in your organization's commitment to innovation and excellence. I look forward to bringing my expertise to your team."
        }
        findViewById<TextView>(R.id.tvWhyCompany)?.text = whyCompanyText

        // Closing & Signature
        findViewById<TextView>(R.id.tvClosing)?.text = "Yours Sincerely,"
        findViewById<TextView>(R.id.tvSignature)?.text = if (name.isNotEmpty()) name else "Your Name"
    }
}