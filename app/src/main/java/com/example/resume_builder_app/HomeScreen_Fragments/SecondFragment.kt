package com.example.resume_builder_app.HomeScreen_Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.resume_builder_app.R

class SecondFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // This links your layout XML to the fragment code
        return inflater.inflate(R.layout.fragment_second, container, false)
    }
}