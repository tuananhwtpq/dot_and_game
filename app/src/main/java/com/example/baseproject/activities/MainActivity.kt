package com.example.baseproject.activities

import android.view.View
import com.example.baseproject.R
import com.example.baseproject.bases.BaseActivity
import com.example.baseproject.databinding.ActivityMainBinding
import com.example.baseproject.utils.gone
import com.example.baseproject.utils.invisible
import com.example.baseproject.utils.showToast
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
    }

    override fun initActionView() {

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
                when (winner) {
                    0 -> {
                        showToast("It's a draw")
                    }

                    1 -> {
                        showToast("Player 1 is winner!")
                    }

                    2 -> {
                        if (selectedAgainst == "computer") showToast("Computer win!") else showToast(
                            "Player 2 win!"
                        )
                    }
                }
            }

        })
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