package com.example.baseproject.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.baseproject.R
import com.example.baseproject.bases.BaseActivity
import com.example.baseproject.databinding.ActivityStartBinding
import com.example.baseproject.utils.gone
import com.example.baseproject.utils.visible

class StartActivity : BaseActivity<ActivityStartBinding>(ActivityStartBinding::inflate) {

    private var diffiTypeSelected = "medium"
    private var selectedSize = 1
    private var selectedAgainst = "computer"

    override fun initData() {
        binding.btn44.isSelected = true
        binding.btnComputer.isSelected = true
        binding.btnEasy.isSelected = true
    }

    override fun initView() {
    }

    override fun initActionView() {
        setupSizeSelect()
        setupPlayAgainst()
        setupDifficultType()

        binding.btnStart.setOnClickListener {
            startButton()
        }
    }

    private fun startButton(){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("selectedSize", selectedSize)
        intent.putExtra("selectedAgainst", selectedAgainst)
        intent.putExtra("selectedLevel", diffiTypeSelected)
        startActivity(intent)
    }

    private fun setupSizeSelect(){
        binding.btn44.setOnClickListener {
            binding.btn44.isSelected = true
            binding.btn66.isSelected = false
            selectedSize = 1
        }

        binding.btn66.setOnClickListener {
            binding.btn44.isSelected = false
            binding.btn66.isSelected = true
            selectedSize = 2
        }
    }

    private fun setupPlayAgainst(){
        binding.btnComputer.setOnClickListener {
            binding.btnHuman.isSelected = false
            binding.btnComputer.isSelected = true
            binding.tvSelectDifficulty.visible()
            binding.layoutDifficultType.visible()
            selectedAgainst = "computer"
        }

        binding.btnHuman.setOnClickListener {
            binding.btnHuman.isSelected = true
            binding.btnComputer.isSelected = false
            binding.tvSelectDifficulty.gone()
            binding.layoutDifficultType.gone()
            selectedAgainst = "human"
            diffiTypeSelected = ""
        }
    }

    private fun setupDifficultType(){
        binding.btnEasy.setOnClickListener {
            binding.btnEasy.isSelected = true
            binding.btnMedium.isSelected = false
            binding.btnHard.isSelected = false
            diffiTypeSelected = "easy"
        }

        binding.btnMedium.setOnClickListener {
            binding.btnEasy.isSelected = false
            binding.btnMedium.isSelected = true
            binding.btnHard.isSelected = false
            diffiTypeSelected = "medium"
        }

        binding.btnHard.setOnClickListener {
            binding.btnEasy.isSelected = false
            binding.btnMedium.isSelected = false
            binding.btnHard.isSelected = true
            diffiTypeSelected = "hard"
        }


    }

}