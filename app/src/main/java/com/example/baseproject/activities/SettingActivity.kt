package com.example.baseproject.activities

import android.content.Intent
import android.net.Uri
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import com.example.baseproject.R
import com.example.baseproject.bases.BaseActivity
import com.example.baseproject.databinding.ActivitySettingBinding
import com.example.baseproject.utils.Constants
import com.example.baseproject.utils.setOnUnDoubleClick
import com.example.baseproject.utils.showToast

class SettingActivity : BaseActivity<ActivitySettingBinding>(ActivitySettingBinding::inflate) {

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun initData() {
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    override fun initView() {
    }

    override fun initActionView() {

        binding.btnLanguage.setOnUnDoubleClick {
            startActivity(Intent(this, LanguageActivity::class.java))
        }

        binding.btnPrivacy.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.PRIVACY_POLICY))
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.btnShareApp.setOnClickListener {
            shareApp()
        }

        binding.btnFeedback.setOnClickListener {
            feedBack()
        }

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun feedBack() {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:".toUri()
                putExtra(Intent.EXTRA_EMAIL, arrayOf(R.string.rating_email))
                putExtra(Intent.EXTRA_SUBJECT, "Feedback ${getString(R.string.app_name)}")
                putExtra(Intent.EXTRA_TEXT, "")
            }
            startActivity(intent)
        } catch (e: Exception) {
            showToast(getString(R.string.no_email_client_installed))
        }
    }

    private fun shareApp() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
            val shareMessage =
                "https://play.google.com/store/apps/details?id=${this.packageName}"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {
            e.printStackTrace()
            showToast(getString(R.string.failed_to_share))
        }
    }

}