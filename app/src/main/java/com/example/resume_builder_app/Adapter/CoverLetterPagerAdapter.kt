package com.example.resume_builder_app.Activities

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.resume_builder_app.Adapters.DetailsFragment
import com.example.resume_builder_app.Adapters.TemplateFragment
import com.example.resume_builder_app.Fragments.PreviewFragment

class CoverLetterPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val fragments = listOf(
        DetailsFragment(),
        TemplateFragment(),
        PreviewFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun getFragment(position: Int): Fragment {
        return fragments[position]
    }
}