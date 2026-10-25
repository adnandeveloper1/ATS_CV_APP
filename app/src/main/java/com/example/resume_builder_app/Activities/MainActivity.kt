package com.example.resume_builder_app.Activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.resume_builder_app.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        val progressbar = findViewById<ProgressBar>(R.id.progressBar)
        progressbar.visibility = View.VISIBLE
        ObjectAnimator.ofInt(progressbar, "progress", 0, 100).apply {
            duration = 1000
            interpolator = DecelerateInterpolator()

            // Listen for when the progress animation completes
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    // Start your onboarding or next activity here
                    val intent = Intent(this@MainActivity, OnBording::class.java)
                    startActivity(intent)
                    // Optional: call finish() so the user cannot press back into the splash/loading screen
                    finish()
                }
            })

            start()
        }
        AnimatedBackground()

    }
     private fun AnimatedBackground(){
        val rootLayout = findViewById<ConstraintLayout>(R.id.main)
        // Define the color transitions
        val colorStart1 = Color.parseColor("#153E7E")
        val colorEnd1 = Color.parseColor("#1E56A0")
        val colorStart2 = Color.parseColor("#0D2247")

        val animator = ValueAnimator.ofFloat(0f, 9f).apply {
            duration = 1000 // Duration for one smooth transition cycle (5 seconds)
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE

            addUpdateListener { animation ->
                val fraction = animation.animatedFraction

                // Evaluate smooth color shifts using ArgbEvaluator
                val evaluators = ArgbEvaluator()
                val currentStart = evaluators.evaluate(fraction, colorStart1, colorStart2) as Int
                val currentEnd = evaluators.evaluate(fraction, colorEnd1, colorStart1) as Int

                // Apply dynamically generated gradient drawable
                val gradientDrawable = GradientDrawable(
                    GradientDrawable.Orientation.TL_BR,
                    intArrayOf(currentStart, currentEnd)
                )
                rootLayout.background = gradientDrawable
            }
        }
        animator.start()
    }
}