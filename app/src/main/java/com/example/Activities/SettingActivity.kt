package com.example.Activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.resumebuilder.R

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                ContextCompat.getColor(this, R.color.blue)
            )
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val rootView = findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val navBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.setPadding(0, 0, 0, navBars.bottom)
            insets
        }

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btncontact = findViewById<LinearLayout>(R.id.Contactus)
        val btnshare = findViewById<LinearLayout>(R.id.ShareApp)
        val btnfollow = findViewById<LinearLayout>(R.id.FollowMe)
        val btnprivacy = findViewById<LinearLayout>(R.id.privacy)

        btnprivacy.setOnClickListener {
            openWebsite(this, "https://sites.google.com/view/resume-builder-ai-cv-maker-ats/home?authuser=0")
        }

        btnBack.setOnClickListener { finish() }

        btncontact.setOnClickListener {
            showCustomContactDialog(this)
        }

        btnshare.setOnClickListener {
            shareApp(this)
        }

        btnfollow.setOnClickListener {
            openLinkedInProfile(this, "PabossMehdi")
        }
    }

    private fun showCustomContactDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_contact_us)

        dialog.window?.attributes?.windowAnimations = R.style.PopDialogAnimation
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val width = (context.resources.displayMetrics.widthPixels * 0.89).toInt()
        dialog.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

        val btnEmail = dialog.findViewById<View>(R.id.btnOptionEmail)
        val btnWhatsApp = dialog.findViewById<View>(R.id.btnOptionWhatsApp)
        val btnCancel = dialog.findViewById<View>(R.id.btnCancel)

        btnEmail.setOnClickListener {
            dialog.dismiss()
            sendEmail(context)
        }

        btnWhatsApp.setOnClickListener {
            dialog.dismiss()
            openWhatsApp(context)
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun sendEmail(context: Context) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("info@nexappra.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Query from App")
        }
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send Email via..."))
        } catch (e: Exception) {
            Toast.makeText(context, "No Email app found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openWhatsApp(context: Context) {
        val phoneNumber = "+923341057125"
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareApp(context: Context) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            val shareMessage = "Check out this amazing app:\nhttps://play.google.com/store/apps/details?id=${context.packageName}"
            putExtra(Intent.EXTRA_TEXT, shareMessage)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share App Via"))
    }

    private fun openLinkedInProfile(context: Context, linkedInId: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://www.linkedin.com/company/nexappra/$linkedInId")
            setPackage("com.linkedin.android")
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.linkedin.com/in/paboss-mehdi-a0b00b265?utm_source=share_via&utm_content=profile&utm_medium=member_android$linkedInId")
            )
            context.startActivity(webIntent)
        }
    }

    private fun openWebsite(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to open link", Toast.LENGTH_SHORT).show()
        }
    }
}