package com.example.baseproject.activities

import android.view.View
import com.example.baseproject.bases.BaseActivity
import com.example.baseproject.databinding.ActivityMainBinding
import com.example.baseproject.utils.gone
import com.example.baseproject.utils.showToast

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val selectedSize by lazy {
        intent.getIntExtra("selectedSize", 1)
    }

    private val selectedAgainst by lazy {
        intent.getStringExtra("selectedAgainst") ?: "computer"
    }

    private val selectedLevel by lazy {
        intent.getStringExtra("selectedLevel") ?: "easy"
    }


    override fun initData() {
        val gridSize = if (selectedSize == 1) 4 else 6
        binding.gameView.setGridSize(gridSize, gridSize)
        binding.gameView.setGameMode(selectedAgainst, selectedLevel)
    }

    override fun initView() {

        binding.tvScore1.text = "0"
        binding.tvScore2.text = "0"
        updateTurnText(1)

        binding.tvPlayer2.text = if (selectedAgainst == "computer") "Computer" else " Player 2"

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
                updateTurnText(currentPlayer)
            }

            override fun onGameOver(winner: Int) {
                when(winner){
                    0 -> {
                        showToast("It's a draw")
                    }
                    1 -> {
                        showToast("Player 1 is winner!")
                    }
                    2 -> {
                        if (selectedAgainst == "computer") showToast("Computer win!") else showToast("Player 2 win!")
                    }
                }
            }

        })
    }

    private fun updateTurnText(currentPlayer: Int) {
        if (currentPlayer == 1) {
            binding.tvTurn.text = "Player 1 turn"
        } else {
            binding.tvTurn.text =
                if (selectedAgainst == "computer") "Computer turn" else "Player 2 turn"
        }
    }


}