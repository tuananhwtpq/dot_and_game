package com.example.baseproject.activities

import androidx.activity.OnBackPressedCallback
import com.example.baseproject.bases.BaseActivity
import com.example.baseproject.databinding.ActivityGuideBinding
import com.example.baseproject.utils.SharedPrefManager

class GuideActivity : BaseActivity<ActivityGuideBinding>(ActivityGuideBinding::inflate) {

    private var isFromHome: Boolean = false

    private var onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isFromHome) {
                finish()
            } else {
                finish()
            }
        }

    }

    override fun initData() {
        isFromHome = SharedPrefManager.getBoolean("isFromHome", false)
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    override fun initView() {
    }

    override fun initActionView() {

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnExit.setOnClickListener {
            finish()
        }
    }

}