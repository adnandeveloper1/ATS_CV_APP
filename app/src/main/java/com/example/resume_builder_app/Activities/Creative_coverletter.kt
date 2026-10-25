package com.example.resume_builder_app.Activities

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.resume_builder_app.R

class Creative_coverletter : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_creative_coverletter)

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

        // Bind data
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

        // Set values
        tvName?.text = name
        tvJobTitle?.text = jobTitle
        tvEmail?.text = email
        tvPhone?.text = phone
        tvCompanyName?.text = companyName

        // Recipient logic
        if (recipientName.isNotEmpty()) {
            tvToHiringManager?.text = "TO ${recipientName.uppercase()}"
            tvGreeting?.text = "Dear $recipientName,"
        } else {
            tvToHiringManager?.text = "TO HIRING MANAGER"
            tvGreeting?.text = "Dear Hiring Manager,"
        }

        // Body text
        tvBody?.text = if (notes.isNotEmpty()) {
            notes
        } else {
            "I am excited to apply for the $jobTitle position at $companyName. " +
                    "Although I do not have direct experience in the industry, I believe my skills in office management, " +
                    "organization, and communication will be highly valuable to your team.\n\n" +
                    "My experience in managing office operations, coordinating schedules, and overseeing administrative tasks " +
                    "will help ensure that your projects run smoothly and efficiently. Additionally, my ability to work well " +
                    "under pressure and to multitask effectively will enable me to handle the demands of a fast-paced environment. " +
                    "Thank you for considering my application. I look forward to discussing my qualifications with you further."
        }

        tvClosing?.text = "Sincerely,"
        tvSignature?.text = name
    }
}