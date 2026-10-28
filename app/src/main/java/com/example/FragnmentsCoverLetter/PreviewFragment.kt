package com.example.resumebuilder.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.resumebuilder.R
import com.example.CoverLetterViewModel
import com.example.resumebuilder.Adapters.JobDetails

class PreviewFragment : Fragment() {

    private lateinit var viewModel: CoverLetterViewModel
    private lateinit var container: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[CoverLetterViewModel::class.java]
        container = view.findViewById(R.id.templateContainer)
    }

    /**
     * This function must be called every time user comes to Preview page
     */
    fun refreshPreview() {
        val template = viewModel.selectedTemplate
        val details = viewModel.jobDetails

        if (details == null) {
            Toast.makeText(requireContext(), "No job details found", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedName = template?.title?.trim()
            ?: template?.type?.trim()
            ?: "Professional"

        Toast.makeText(requireContext(), "Loading Template: $selectedName", Toast.LENGTH_SHORT).show()

        val layoutId = when (selectedName) {
            "Professional" -> R.layout.activity_professional_coverletter
            "Modern"       -> R.layout.activity_creative_coverletter
           "Minimal"      -> R.layout.activity_minimal_coverletter
            else           -> R.layout.activity_professional_coverletter
        }

        container.removeAllViews()
        val templateView = layoutInflater.inflate(layoutId, container, false)
        container.addView(templateView)

        bindData(templateView, details)
    }

    private fun bindData(view: View, details: JobDetails) {
        view.findViewById<TextView>(R.id.tvName)?.text = details.userName
        view.findViewById<TextView>(R.id.tvJobTitle)?.text = details.jobTitle.uppercase()
        view.findViewById<TextView>(R.id.tvEmail)?.text = details.userEmail
        view.findViewById<TextView>(R.id.tvPhone)?.text = details.contactNum
        view.findViewById<TextView>(R.id.tvCompanyName)?.text = details.companyName

        val tvTo = view.findViewById<TextView>(R.id.tvToHiringManager)
        val tvGreeting = view.findViewById<TextView>(R.id.tvGreeting)

        if (details.recipientName.isNotEmpty()) {
            tvTo?.text = "TO ${details.recipientName.uppercase()}"
            tvGreeting?.text = "Dear ${details.recipientName},"
        } else {
            tvTo?.text = "TO HIRING MANAGER"
            tvGreeting?.text = "Dear Hiring Manager,"
        }

        val body = if (details.notes.isNotEmpty()) {
            details.notes
        } else {
            "I am excited to apply for the ${details.jobTitle} position at ${details.companyName}. " +
                    "Although I do not have direct experience in the industry, I believe my skills in office management, " +
                    "organization, and communication will be highly valuable to your team.\n\n" +
                    "My experience in managing office operations, coordinating schedules, and overseeing administrative tasks " +
                    "will help ensure that your projects run smoothly and efficiently. Additionally, my ability to work well " +
                    "under pressure and to multitask effectively will enable me to handle the demands of a fast-paced environment. " +
                    "Thank you for considering my application. I look forward to discussing my qualifications with you further."
        }

        view.findViewById<TextView>(R.id.tvBody)?.text = body
        view.findViewById<TextView>(R.id.tvClosing)?.text = "Sincerely,"
        view.findViewById<TextView>(R.id.tvSignature)?.text = details.userName
    }

    fun getTemplateView(): View? {
        return if (::container.isInitialized && container.childCount > 0) {
            container.getChildAt(0)
        } else {
            null
        }
    }
}