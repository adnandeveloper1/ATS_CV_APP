package com.example.resume_builder_app.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.resume_builder_app.HomeScreen_Fragments.OneScreenFragment
import com.example.resume_builder_app.HomeScreen_Fragments.SecondFragment
import com.example.resume_builder_app.HomeScreen_Fragments.ThirdFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3 // Total number of pages

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OneScreenFragment()
            1 -> SecondFragment()
            else -> ThirdFragment()
        }
    }
}