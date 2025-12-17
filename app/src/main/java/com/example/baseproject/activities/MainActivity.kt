package com.example.baseproject.activities

import android.content.Intent
import androidx.activity.OnBackPressedCallback
import com.example.baseproject.R
import com.example.baseproject.bases.BaseActivity
import com.example.baseproject.databinding.ActivityMainBinding
import com.example.baseproject.fragments.ExitGameDialog
import com.example.baseproject.utils.invisible
import com.example.baseproject.utils.setOnUnDoubleClick
import com.example.baseproject.utils.visible

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val selectedSize by lazy {
        intent.getIntExtra("selectedSize", 1)
    }

    private val selectedAgainst by lazy {
        intent.getStringExtra("selectedAgainst") ?: "computer"
    }

    private val selectedLevel by lazy {
        intent.getStringExtra("selectedLevel") ?: "hard"
    }

    private var onBackPressCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val dialog = ExitGameDialog.newInstance()
            dialog.show(supportFragmentManager, ExitGameDialog.TAG)
        }
    }


    override fun initData() {
        val gridSize = when (selectedSize) {
            1 -> 4
            2 -> 6
            else -> 8
        }
        binding.gameView.setGridSize(gridSize, gridSize)
        binding.gameView.setGameMode(selectedAgainst, selectedLevel)

        if (selectedAgainst == "computer") {
            binding.ivPlayer2.setImageResource(R.drawable.iv_robot)
        }
    }

    override fun initView() {

        binding.tvScore1.text = "0"
        binding.tvScore2.text = "0"
        binding.btnDownPl1.visible()
        binding.btnDownPl2.invisible()

        onBackPressedDispatcher.addCallback(onBackPressCallback)
    }

    override fun initActionView() {
        handleGameView()
        handleButtonClick()
    }

    private fun handleButtonClick() {
        binding.btnBack.setOnUnDoubleClick {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnGuide.setOnUnDoubleClick {
            val intent = Intent(this, GuideActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleGameView() {
        binding.gameView.setListener(object : GameView.GameListener {
            override fun onGameUpdate(
                player1Score: Int,
                player2Score: Int,
                currentPlayer: Int
            ) {
                binding.tvScore1.text = player1Score.toString()
                binding.tvScore2.text = player2Score.toString()
                updateBottomArrow(currentPlayer)
            }

            override fun onGameOver(winner: Int) {

                binding.root.postDelayed({
                    val intent = Intent(this@MainActivity, ResultActivity::class.java)
                    intent.putExtra("winner", winner)
                    intent.putExtra("score1", binding.tvScore1.text.toString().toInt())
                    intent.putExtra("score2", binding.tvScore2.text.toString().toInt())
                    intent.putExtra("mode", selectedAgainst)
                    intent.putExtra("selectedSize", selectedSize)
                    intent.putExtra("selectedLevel", selectedLevel)
                    startActivity(intent)
                    finish()
                }, 500)
            }

            override fun onExtraTurn(player: Int) {
                showExtraTurnAnimation(player)
            }

        })
    }

    private fun showExtraTurnAnimation(player: Int) {

        if (selectedAgainst == "computer" && player == 2) {
            return
        }

        if (selectedAgainst == "computer") {
            binding.playerTurn.text = getString(R.string.you_get_an_extra_turn)
        } else {
            if (player == 1) {
                binding.playerTurn.text = getString(R.string.blue_gets_an_extra_turn)
            } else {
                binding.playerTurn.text = getString(R.string.red_gets_an_extra_turn)
            }
        }

        binding.layoutOverlay.visible()
        binding.layoutOverlay.alpha = 0f
        binding.layoutOverlay.animate().alpha(1f).setDuration(300).start()

        binding.layoutOverlay.postDelayed({
            binding.layoutOverlay.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction {
                    binding.layoutOverlay.invisible()
                }
                .start()
        }, 500)
    }

    private fun updateBottomArrow(currentPlayer: Int) {
        if (currentPlayer == 1) {
            binding.btnDownPl1.visible()
            binding.btnDownPl2.invisible()
        } else {
            binding.btnDownPl1.invisible()
            binding.btnDownPl2.visible()
        }
    }

}