package com.example.Activities

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.resume_builder.Dataclass.ResumeRepository
import com.example.resume_builder.Dataclass.SharedResumeViewModel
import com.example.resumebuilder.Activities.PersonalFragment
import com.example.resumebuilder.R
import com.example.resumebuilder.ResumeFragnments.EducationFragment
import com.example.resumebuilder.ResumeFragnments.ExperienceFragment
import com.example.resumebuilder.ResumeFragnments.SkillsFragment
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson

class ResumeActivity : AppCompatActivity() {

    private var currentStep = 1
    private val sharedViewModel: SharedResumeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resume)

        // Window Insets listener updated to push layout above keyboard (IME)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeBars = insets.getInsets(WindowInsetsCompat.Type.ime())

            val bottomPadding = Math.max(systemBars.bottom, imeBars.bottom)

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                bottomPadding
            )
            insets
        }

        val btnNext = findViewById<MaterialButton>(R.id.btnNext)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        // Load initial fragment
        if (savedInstanceState == null) {
            loadFragment(PersonalFragment())
            updateStepUI()
        }

        btnNext.setOnClickListener {
            // Validate Step 1 before advancing
            if (currentStep == 1) {
                val personalFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as? PersonalFragment
                if (personalFragment?.validateAndSave() == false) {
                    return@setOnClickListener // Stop progression if validation fails
                }
            }

            if (currentStep < 4) {
                currentStep++
                updateStepUI()
            } else {
                // Step 4: Finish & Forward to Templates Selection
                val resumeTitle = if (ResumeRepository.name.isNotEmpty()) {
                    "${ResumeRepository.name}'s Resume"
                } else {
                    "My Resume"
                }

                // 1. Convert active repository data to JSON payload
                val resumeModel = ResumeRepository.toModel()
                val resumeDataJson = Gson().toJson(resumeModel)

                // 2. Open Templates Selection Screen
                val intent = Intent(this, TemplatesActivity::class.java).apply {
                    putExtra("docTitle", resumeTitle)
                    putExtra("dataJson", resumeDataJson)
                }
                startActivity(intent)

                // Close current creation screen
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

    // --- REUSABLE DOWNLOAD SUCCESS DIALOG ---
    fun showDownloadSuccessDialog() {
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
        tvMessage.text = "Your resume has been downloaded and saved to My Documents."

        btnOk.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun updateStepUI() {
        val step1Num = findViewById<TextView>(R.id.step1Num)
        val step2Num = findViewById<TextView>(R.id.step2Num)
        val step3Num = findViewById<TextView>(R.id.step3Num)
        val step4Num = findViewById<TextView>(R.id.step4Num)

        val step1Text = findViewById<TextView>(R.id.step1Text)
        val step2Text = findViewById<TextView>(R.id.step2Text)
        val step3Text = findViewById<TextView>(R.id.step3Text)
        val step4Text = findViewById<TextView>(R.id.step4Text)

        val stepCircles = listOfNotNull(step1Num, step2Num, step3Num, step4Num)
        val stepLabels = listOfNotNull(step1Text, step2Text, step3Text, step4Text)

        val activeColor = Color.parseColor("#2563EB")
        val inactiveColor = Color.parseColor("#94A3B8")

        for (i in stepCircles.indices) {
            if (i + 1 == currentStep) {
                stepCircles[i].setBackgroundResource(R.drawable.bg_icon_circle)
                stepCircles[i].backgroundTintList = ColorStateList.valueOf(activeColor)
                stepCircles[i].setTextColor(Color.WHITE)

                if (i < stepLabels.size) {
                    stepLabels[i].setTextColor(activeColor)
                }
            } else {
                stepCircles[i].setBackgroundResource(R.drawable.bg_icon_circle)
                stepCircles[i].backgroundTintList = null
                stepCircles[i].setTextColor(inactiveColor)

                if (i < stepLabels.size) {
                    stepLabels[i].setTextColor(inactiveColor)
                }
            }
        }

        when (currentStep) {
            1 -> loadFragment(PersonalFragment())
            2 -> loadFragment(EducationFragment())
            3 -> loadFragment(ExperienceFragment())
            4 -> loadFragment(SkillsFragment())
        }

        val btnNext = findViewById<MaterialButton>(R.id.btnNext)
        btnNext?.text = if (currentStep == 4) "Choose Template" else "Next"
    }
}