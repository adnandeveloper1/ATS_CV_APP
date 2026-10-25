package com.example.resume_builder_app.Activities

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.resume_builder.Dataclass.SharedResumeViewModel
import com.example.resume_builder_app.R
import com.example.resume_builder_app.fragments.EducationFragment
import com.example.resume_builder_app.fragments.ExperienceFragment
import com.example.resume_builder_app.fragments.SkillsFragment
import com.google.android.material.button.MaterialButton

class ResumeActivity : AppCompatActivity() {

    private var currentStep = 1
    private val sharedViewModel: SharedResumeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resume)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnNext = findViewById<MaterialButton>(R.id.btnNext)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        // Load step 1 initially
        if (savedInstanceState == null) {
            loadFragment(PersonalFragment())
            updateStepUI() // Ensure step 1 UI colors are applied on launch
        }

        btnNext.setOnClickListener {
            if (currentStep < 4) {
                currentStep++
                updateStepUI()
            } else {
                // Final step reached (Step 4 -> Finish & Save)
                Toast.makeText(this, "Resume Saved Successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, TemplatesActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        btnBack.setOnClickListener {
            if (currentStep > 1) {
                currentStep--
                updateStepUI()
            } else {
                finish()
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun updateStepUI() {
        // 1. Circle Number Views
        val step1Num = findViewById<TextView>(R.id.step1Num)
        val step2Num = findViewById<TextView>(R.id.step2Num)
        val step3Num = findViewById<TextView>(R.id.step3Num)
        val step4Num = findViewById<TextView>(R.id.step4Num)

        // 2. Text Label Views underneath circles
        val step1Text = findViewById<TextView>(R.id.step1Text)
        val step2Text = findViewById<TextView>(R.id.step2Text)
        val step3Text = findViewById<TextView>(R.id.step3Text)
        val step4Text = findViewById<TextView>(R.id.step4Text)

        // Safely map non-null views
        val stepCircles = listOfNotNull(step1Num, step2Num, step3Num, step4Num)
        val stepLabels = listOfNotNull(step1Text, step2Text, step3Text, step4Text)

        val activeColor = Color.parseColor("#2563EB")
        val inactiveColor = Color.parseColor("#94A3B8")

        // Loop over step views
        for (i in stepCircles.indices) {
            if (i + 1 == currentStep) {
                // Active Step Styling
                stepCircles[i].setBackgroundResource(R.drawable.bg_icon_circle)
                stepCircles[i].backgroundTintList = ColorStateList.valueOf(activeColor)
                stepCircles[i].setTextColor(Color.WHITE)

                if (i < stepLabels.size) {
                    stepLabels[i].setTextColor(activeColor)
                }
            } else {
                // Inactive Step Styling
                stepCircles[i].setBackgroundResource(R.drawable.bg_icon_circle)
                stepCircles[i].backgroundTintList = null
                stepCircles[i].setTextColor(inactiveColor)

                if (i < stepLabels.size) {
                    stepLabels[i].setTextColor(inactiveColor)
                }
            }
        }

        // Swap out fragments based on active step
        when (currentStep) {
            1 -> loadFragment(PersonalFragment())
            2 -> loadFragment(EducationFragment())
            3 -> loadFragment(ExperienceFragment())
            4 -> loadFragment(SkillsFragment())
        }

        // Update button text on step 4
        val btnNext = findViewById<MaterialButton>(R.id.btnNext)
        btnNext?.text = if (currentStep == 4) "Finish & Save" else "Next"
    }
}