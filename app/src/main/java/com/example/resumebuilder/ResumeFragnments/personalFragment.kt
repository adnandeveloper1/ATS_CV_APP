package com.example.resumebuilder.Activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.resume_builder.Dataclass.ResumeRepository
import com.example.resume_builder.Dataclass.SharedResumeViewModel
import com.example.resumebuilder.R

class PersonalFragment : Fragment() {

    private val sharedViewModel: SharedResumeViewModel by activityViewModels()

    private var etName: EditText? = null
    private var etAddress: EditText? = null
    private var etEmail: EditText? = null
    private var etPhone: EditText? = null
    private var etRole: EditText? = null
    private var etSummary: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind Views
        etName = view.findViewById(R.id.et_name)
        etAddress = view.findViewById(R.id.etLocation)
        etEmail = view.findViewById(R.id.et_email)
        etPhone = view.findViewById(R.id.et_Phone)
        etRole = view.findViewById(R.id.role)
        etSummary = view.findViewById(R.id.introduction)

        // Restore Data
        etName?.setText(sharedViewModel.name.value ?: ResumeRepository.name)
        etAddress?.setText(sharedViewModel.address.value ?: ResumeRepository.address)
        etEmail?.setText(sharedViewModel.email.value ?: ResumeRepository.email)
        etPhone?.setText(sharedViewModel.phone.value ?: ResumeRepository.phone)
        etRole?.setText(sharedViewModel.role.value ?: ResumeRepository.role)
        etSummary?.setText(sharedViewModel.summary.value ?: ResumeRepository.summary)

        // Auto Save to Repository on typing
        etName?.addTextChangedListener(createWatcher { ResumeRepository.name = it })
        etAddress?.addTextChangedListener(createWatcher { ResumeRepository.address = it })
        etEmail?.addTextChangedListener(createWatcher { ResumeRepository.email = it })
        etPhone?.addTextChangedListener(createWatcher { ResumeRepository.phone = it })
        etRole?.addTextChangedListener(createWatcher { ResumeRepository.role = it })
        etSummary?.addTextChangedListener(createWatcher { ResumeRepository.summary = it })
    }

    /**
     * Called by ResumeActivity to validate input before proceeding
     */
    fun validateAndSave(): Boolean {
        val name = etName?.text.toString().trim()
        val email = etEmail?.text.toString().trim()
        val phone = etPhone?.text.toString().trim()
        val role = etRole?.text.toString().trim()

        if (name.isEmpty()) {
            etName?.error = "Full Name is required"
            etName?.requestFocus()
            return false
        }

        if (email.isEmpty()) {
            etEmail?.error = "Email is required"
            etEmail?.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail?.error = "Enter a valid email address"
            etEmail?.requestFocus()
            return false
        }

        if (phone.isEmpty()) {
            etPhone?.error = "Phone number is required"
            etPhone?.requestFocus()
            return false
        }

        if (role.isEmpty()) {
            etRole?.error = "Job Title / Role is required"
            etRole?.requestFocus()
            return false
        }

        // Store into ViewModel & Repository
        sharedViewModel.name.value = name
        sharedViewModel.email.value = email
        sharedViewModel.phone.value = phone
        sharedViewModel.role.value = role
        sharedViewModel.address.value = etAddress?.text.toString().trim()
        sharedViewModel.summary.value = etSummary?.text.toString().trim()

        return true
    }

    private fun createWatcher(onChanged: (String) -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                onChanged(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }
}