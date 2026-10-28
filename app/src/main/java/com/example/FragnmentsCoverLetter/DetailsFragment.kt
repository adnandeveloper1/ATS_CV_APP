package com.example.resumebuilder.Adapters   // keep your package name

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.resumebuilder.R
import com.google.android.material.textfield.TextInputEditText

class DetailsFragment : Fragment() {

    private lateinit var etUserName: TextInputEditText
    private lateinit var etUserEmail: TextInputEditText
    private lateinit var etContactNum: TextInputEditText
    private lateinit var etJobTitle: TextInputEditText
    private lateinit var etCompanyName: TextInputEditText
    private lateinit var etRecipientName: TextInputEditText
    private lateinit var etAdditionalNotes: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        etUserName = view.findViewById(R.id.etuserName)
        etUserEmail = view.findViewById(R.id.etuseremail)
        etContactNum = view.findViewById(R.id.etContactnum)
        etJobTitle = view.findViewById(R.id.etJobTitle)
        etCompanyName = view.findViewById(R.id.etCompanyName)
        etRecipientName = view.findViewById(R.id.etRecipientName)
        etAdditionalNotes = view.findViewById(R.id.etAdditionalNotes)
    }

    /**
     * Validates all required fields and returns JobDetails object.
     * Returns null if any required field is invalid.
     */
    fun validateAndGetDetails(): JobDetails? {

        val userName = etUserName.text.toString().trim()
        val userEmail = etUserEmail.text.toString().trim()
        val contactNum = etContactNum.text.toString().trim()
        val jobTitle = etJobTitle.text.toString().trim()
        val companyName = etCompanyName.text.toString().trim()
        val recipientName = etRecipientName.text.toString().trim()
        val notes = etAdditionalNotes.text.toString().trim()

        // Validation
        if (userName.isEmpty()) {
            etUserName.error = "Your Name is required"
            etUserName.requestFocus()
            return null
        }

        if (userEmail.isEmpty()) {
            etUserEmail.error = "Email is required"
            etUserEmail.requestFocus()
            return null
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            etUserEmail.error = "Please enter a valid email address"
            etUserEmail.requestFocus()
            return null
        }

        if (contactNum.isEmpty()) {
            etContactNum.error = "Contact Number is required"
            etContactNum.requestFocus()
            return null
        }

        if (jobTitle.isEmpty()) {
            etJobTitle.error = "Job Title is required"
            etJobTitle.requestFocus()
            return null
        }

        if (companyName.isEmpty()) {
            etCompanyName.error = "Company Name is required"
            etCompanyName.requestFocus()
            return null
        }

        // All good
        return JobDetails(
            userName = userName,
            userEmail = userEmail,
            contactNum = contactNum,
            jobTitle = jobTitle,
            companyName = companyName,
            recipientName = recipientName,
            notes = notes
        )
    }
}

// Data class (keep it in the same file or move to a separate file)
data class JobDetails(
    val userName: String,
    val userEmail: String,
    val contactNum: String,
    val jobTitle: String,
    val companyName: String,
    val recipientName: String,
    val notes: String
)