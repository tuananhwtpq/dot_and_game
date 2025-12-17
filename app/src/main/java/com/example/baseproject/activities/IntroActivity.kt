package com.example.baseproject.activities

import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.viewpager2.widget.ViewPager2
import com.example.baseproject.adapters.IntroViewPagerAdapter
import com.example.baseproject.bases.BaseActivity
import com.example.baseproject.databinding.ActivityIntroBinding
import com.example.baseproject.utils.SharedPrefManager

class IntroActivity : BaseActivity<ActivityIntroBinding>(ActivityIntroBinding::inflate) {

    private val mAdapter by lazy {
        IntroViewPagerAdapter(
            this
        )
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.vpIntro.currentItem in 1..2) {
                binding.vpIntro.currentItem -= 1
            } else {
                finish()
            }
        }
    }

    override fun initData() {
        SharedPrefManager.putBoolean("wantShowRate", false)
        //mAdapter = IntroViewPagerAdapter(this)
    }

    override fun initView() {
        binding.vpIntro.adapter = mAdapter

        val onPageChangeCallback: ViewPager2.OnPageChangeCallback =
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            }
        binding.vpIntro.registerOnPageChangeCallback(onPageChangeCallback)

    }

    override fun initActionView() {
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    fun nextPage() {

        if (binding.vpIntro.currentItem < mAdapter.itemCount - 1) {
            binding.vpIntro.currentItem++
        } else {
//            showInterIntro {
            val intent = Intent(this@IntroActivity, StartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
//            }
        }
    }

    private fun goToHome() {
        startActivity(Intent(this@IntroActivity, MainActivity::class.java))
        finish()
    }
}