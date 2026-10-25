package com.example.resume_builder_app.Activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.resume_builder.Dataclass.ResumeRepository
import com.example.resume_builder.Dataclass.SharedResumeViewModel
import com.example.resume_builder_app.R

class PersonalFragment : Fragment() {

    private val sharedViewModel: SharedResumeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etName = view.findViewById<EditText>(R.id.et_name)
        val etAddress = view.findViewById<EditText>(R.id.etLocation)
        val etEmail = view.findViewById<EditText>(R.id.et_email)
        val etPhone = view.findViewById<EditText>(R.id.et_Phone)
        val etRole = view.findViewById<EditText>(R.id.role)
        val etSummary = view.findViewById<EditText>(R.id.introduction)

        // Restore previous data if available
        etName?.setText(sharedViewModel.name.value ?: "")
        etAddress?.setText(sharedViewModel.address.value ?: "")
        etEmail?.setText(sharedViewModel.email.value ?: "")
        etPhone?.setText(sharedViewModel.phone.value ?: "")
        etRole?.setText(sharedViewModel.role.value ?: "")
        etSummary?.setText(sharedViewModel.summary.value ?: "")

        // Inside onViewCreated:
        etName?.addTextChangedListener(createWatcher { ResumeRepository.name = it })
        etAddress?.addTextChangedListener(createWatcher { ResumeRepository.address = it })
        etEmail?.addTextChangedListener(createWatcher { ResumeRepository.email = it })
        etPhone?.addTextChangedListener(createWatcher { ResumeRepository.phone = it })
        etRole?.addTextChangedListener(createWatcher { ResumeRepository.role = it })
        etSummary?.addTextChangedListener(createWatcher { ResumeRepository.summary = it })
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