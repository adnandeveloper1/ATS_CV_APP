package com.example.resumebuilder.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.resumebuilder.Adapter.ViewPagerAdapter
import com.example.resumebuilder.R

class OnBording : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_on_bording)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val dot1 = findViewById<View>(R.id.dot1)
        val dot2 = findViewById<View>(R.id.dot2)
        val dot3 = findViewById<View>(R.id.dot3)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val tvSkip = findViewById<TextView>(R.id.tvSkip)

        // Set adapter
        viewPager.adapter = ViewPagerAdapter(this)

        // Disable user swiping (pages move ONLY when Next button is pressed)
        viewPager.isUserInputEnabled = false

        // Listen for page changes to update indicators and button text
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                // 1. Reset all dots to inactive
                dot1.setBackgroundResource(R.drawable.bg_dot_inactive)
                dot2.setBackgroundResource(R.drawable.bg_dot_inactive)
                dot3.setBackgroundResource(R.drawable.bg_dot_inactive)

                // 2. Light up the dot corresponding to the current page
                when (position) {
                    0 -> dot1.setBackgroundResource(R.drawable.bg_dot_active)
                    1 -> dot2.setBackgroundResource(R.drawable.bg_dot_active)
                    2 -> dot3.setBackgroundResource(R.drawable.bg_dot_active)
                }

                // 3. Optional: Change button text on the final page
                if (position == 2) {
                    btnNext.text = "Get Started"
                    tvSkip.text = "Get Started"
                } else {
                    btnNext.text = "Next"
                    tvSkip.text = "Skip"
                }
            }
        })

        // Handle Next Button Click
        btnNext.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem < 2) {
                // Move to the next fragment smoothly
                viewPager.setCurrentItem(currentItem + 1, true)
            } else {
                // Last page reached -> Navigate to Main Activity
                navigateToMain()
            }
        }

        // Handle Skip Button Click
        tvSkip.setOnClickListener {
            navigateToMain()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // Prevents returning to onboarding when pressing the back button
    }
}