package com.example.baseproject.activities

import android.content.Intent
import android.graphics.Color
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.example.baseproject.R
import com.example.baseproject.bases.BaseActivity
import com.example.baseproject.databinding.ActivityResultBinding
import com.example.baseproject.utils.gone
import com.example.baseproject.utils.setOnUnDoubleClick

class ResultActivity : BaseActivity<ActivityResultBinding>(ActivityResultBinding::inflate) {
    private var winner = 0
    private var score1 = 0
    private var score2 = 0
    private var isPvE = false

    private val selectedSize by lazy {
        intent.getIntExtra("selectedSize", 1)
    }

    private val selectedAgainst by lazy {
        intent.getStringExtra("mode") ?: "computer"
    }

    private val selectedLevel by lazy {
        intent.getStringExtra("selectedLevel") ?: "hard"
    }

    override fun initData() {
        winner = intent.getIntExtra("winner", 1)
        score1 = intent.getIntExtra("score1", 0)
        score2 = intent.getIntExtra("score2", 0)
        val playMode = intent.getStringExtra("mode") ?: "computer"
        isPvE = (playMode == "computer")
    }

    override fun initView() {
        setupResultUI()
    }

    override fun initActionView() {
        binding.btnHome.setOnUnDoubleClick {
            val intent = Intent(this, StartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        binding.btnReplay.setOnUnDoubleClick {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("selectedSize", selectedSize)
            intent.putExtra("selectedAgainst", selectedAgainst)
            intent.putExtra("selectedLevel", selectedLevel)
            startActivity(intent)
            finish()
        }

        binding.root.postDelayed({
            animateToFullScreen()
        }, 1500)
    }

    private fun setupResultUI() {
        val colorBlue = Color.parseColor("#5CA7FF")
        val colorRed = Color.parseColor("#F55D5D")

        if (winner == 1) {
            if (isPvE) {

                binding.layoutWinner.setBackgroundColor(colorBlue)
                binding.tvWinnerText.text = getString(R.string.you_win)
                binding.tvWinnerScore.text = "$score1"
                binding.layoutLoser.gone()

                val constraintSet = ConstraintSet()
                constraintSet.clone(binding.main)
                constraintSet.setGuidelinePercent(binding.guidelineSplit.id, 1.0f)
                constraintSet.applyTo(binding.main)
            } else {
                binding.layoutWinner.setBackgroundColor(colorBlue)
                binding.tvWinnerText.text = getString(R.string.blue_win)
                binding.tvWinnerScore.text = "$score1"

                binding.layoutLoser.setBackgroundColor(colorRed)
                binding.tvLoserText.text = getString(R.string.red_lose)
                binding.tvLoserScore.text = "$score2"
            }
        } else if (winner == 2) {
            if (isPvE) {

                binding.layoutWinner.setBackgroundColor(colorBlue)
                binding.tvWinnerText.text = getString(R.string.you_lose)
                binding.tvWinnerScore.text = "$score1"
                binding.tvWinnerText.setTextColor(
                    ContextCompat.getColor(
                        this@ResultActivity,
                        R.color.lose_color
                    )
                )
                binding.layoutLoser.gone()
                binding.ivCrown.setImageResource(R.drawable.lose_crown)

                val constraintSet = ConstraintSet()
                constraintSet.clone(binding.main)
                constraintSet.setGuidelinePercent(binding.guidelineSplit.id, 1.0f)
                constraintSet.applyTo(binding.main)
            } else {
                binding.layoutWinner.setBackgroundColor(colorRed)
                binding.tvWinnerText.text = getString(R.string.red_win)
                binding.tvWinnerScore.text = "$score2"

                binding.layoutLoser.setBackgroundColor(colorBlue)
                binding.tvLoserText.text = getString(R.string.blue_lose)
                binding.tvLoserScore.text = "$score1"
            }
        } else {
            //Draw
            binding.tvWinnerText.text = getString(R.string.draw)
            binding.tvWinnerScore.text = "$score1 - $score2"
            binding.layoutLoser.visibility = View.GONE
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.main)
            constraintSet.setGuidelinePercent(binding.guidelineSplit.id, 1.0f)
            constraintSet.applyTo(binding.main)
        }
    }

    private fun animateToFullScreen() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.main)
        constraintSet.setGuidelinePercent(binding.guidelineSplit.id, 1.0f)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 800

        TransitionManager.beginDelayedTransition(binding.main, transition)
        constraintSet.applyTo(binding.main)
    }

}