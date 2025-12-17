package com.example.baseproject.activities

import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Intent
import android.view.animation.DecelerateInterpolator
import androidx.activity.OnBackPressedCallback
import com.example.baseproject.bases.BaseActivity
import com.example.baseproject.databinding.ActivitySplashBinding
import com.example.baseproject.utils.Constants
import com.example.baseproject.utils.SharedPrefManager
import com.example.baseproject.utils.invisible
import com.example.baseproject.utils.visible
import com.snake.squad.adslib.AdmobLib
import com.snake.squad.adslib.cmp.GoogleMobileAdsConsentManager
import com.snake.squad.adslib.utils.AdsHelper
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.system.exitProcess

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private var isMobileAdsInitializeCalled = AtomicBoolean(false)
    private var isInitAds = AtomicBoolean(false)

    private var progressAnimator: ValueAnimator? = null
    private val MAX_LOADING_TIME = 8000L

    override fun initData() {
        if (!isTaskRoot
            && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
            && intent.action != null
            && intent.action == Intent.ACTION_MAIN
        ) {
            finish()
            return
        }
    }

    override fun initView() {
        if (AdsHelper.isNetworkConnected(this)) {
            binding.tvLoadingAds.visible()
            setupCMP() // sau khi bat init remote thi xoa cai nay di
            startProgressAnimation()
//            initRemoteConfig()
        } else {
            binding.tvLoadingAds.invisible()
//            Handler(Looper.getMainLooper()).postDelayed({
//                replaceActivity()
//            }, 3000)
            runProgressToFinish()
        }

        binding.seekBar.setOnTouchListener { _, _ -> true }
    }

    override fun initActionView() {
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun setupCMP() {
        val googleMobileAdsConsentManager = GoogleMobileAdsConsentManager(this)
        googleMobileAdsConsentManager.gatherConsent { error ->
            error?.let {
                initializeMobileAdsSdk()
            }

            if (googleMobileAdsConsentManager.canRequestAds) {
                initializeMobileAdsSdk()
            }
        }
    }

    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.get()) {
            //start action
            return
        }
        isMobileAdsInitializeCalled.set(true)
        initAds()
    }

//    private fun initRemoteConfig() {
//        RemoteConfig.initRemoteConfig(this, initListener = object : RemoteConfig.InitListener {
//            override fun onComplete() {
//                RemoteConfig.getAllRemoteValueToLocal()
//                if (isInitAds.get()) {
//                    return
//                }
//                isInitAds.set(true)
//                setupCMP()
//            }
//
//            override fun onFailure() {
//                RemoteConfig.getDefaultRemoteValue()
//                setupCMP()
//            }
//        })
//    }

    private fun initAds() {
        AdmobLib.initialize(this, isDebug = true, isShowAds = false, onInitializedAds = {
            if (it) {
                // todo: fix here
                binding.tvLoadingAds.invisible()
//                Handler(Looper.getMainLooper()).postDelayed({
//                    replaceActivity()
//                }, 5000)
                onAdLoaded()
            } else {
                binding.tvLoadingAds.invisible()
//                Handler(Looper.getMainLooper()).postDelayed({
//                    replaceActivity()
//                }, 5000)
                onAdLoaded()
            }
        })
    }

    private fun runProgressToFinish() {
        val currentProgress = binding.seekBar.progress
        val finishAnimator = ValueAnimator.ofInt(currentProgress, 100)
        finishAnimator.duration = 500

        finishAnimator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            updateProgressUI(progress)
        }

        finishAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                replaceActivity()
            }
        })
        finishAnimator.start()
    }

    private fun startProgressAnimation() {
        progressAnimator = ValueAnimator.ofInt(0, 90)
        progressAnimator?.duration = MAX_LOADING_TIME
        progressAnimator?.interpolator = DecelerateInterpolator()

        progressAnimator?.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            updateProgressUI(progress)
        }
        progressAnimator?.start()

//        progressAnimator?.addListener(object : AnimatorListenerAdapter() {
//            override fun onAnimationEnd(animation: android.animation.Animator) {
//                runProgressToFinish()
//            }
//        })
    }

    private fun updateProgressUI(progress: Int) {
        binding.seekBar.progress = progress
    }

    private fun replaceActivity() {
        SharedPrefManager.putBoolean("wantShowRate", false)
        val isShowedLanguage = SharedPrefManager.getBoolean("isShowedLanguage")

        val nextIntent = if (!isShowedLanguage) {
            Intent(this@SplashActivity, LanguageActivity::class.java)
        } else {
            Intent(this@SplashActivity, StartActivity::class.java)
        }

        nextIntent.putExtra(Constants.LANGUAGE_EXTRA, false)
        startActivity(nextIntent)
        finish()
    }

    private fun onAdLoaded() {
        progressAnimator?.removeAllListeners()
        progressAnimator?.cancel()
        runProgressToFinish()
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            exitProcess(0)
        }
    }

}