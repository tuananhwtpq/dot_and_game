package com.example.baseproject.activities

import android.content.Intent
import com.example.baseproject.bases.BaseActivity
import com.example.baseproject.databinding.ActivityStartBinding
import com.example.baseproject.fragments.ChooseLevelDialog
import com.example.baseproject.utils.SharedPrefManager
import com.example.baseproject.utils.setOnUnDoubleClick

class StartActivity : BaseActivity<ActivityStartBinding>(ActivityStartBinding::inflate) {

    private var selectedLevel = "hard"
    private var selectedSize = 1
    private var selectedAgainst = "computer"

    override fun initData() {
        binding.btn44.isSelected = true
        binding.tvPlayWithFriend.isSelected = true
        binding.tvPlayWithAI.isSelected = true
    }

    override fun initView() {

    }

    override fun initActionView() {
        setupSizeSelect()

        binding.btnHuman.setOnClickListener {
            selectedAgainst = "human"
            goToMain()
        }

        binding.btnComputer.setOnClickListener {
            selectedAgainst = "computer"
            showLevelDialog()
        }

        binding.btnSetting.setOnUnDoubleClick {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        binding.btnGuide.setOnUnDoubleClick {
            SharedPrefManager.putBoolean("isFromHome", true)
            val intent = Intent(this, GuideActivity::class.java)
            startActivity(intent)
        }

        binding.tvGridSize.isSelected = true
        binding.tvPlayWithFriend.isSelected = true
        binding.tvPlayWithAI.isSelected = true
    }

    private fun setupSizeSelect() {
        binding.btn44.setOnClickListener {
            binding.btn44.isSelected = true
            binding.btn66.isSelected = false
            binding.btn88.isSelected = false
            selectedSize = 1
        }

        binding.btn66.setOnClickListener {
            binding.btn44.isSelected = false
            binding.btn66.isSelected = true
            binding.btn88.isSelected = false
            selectedSize = 2
        }

        binding.btn88.setOnClickListener {
            binding.btn44.isSelected = false
            binding.btn66.isSelected = false
            binding.btn88.isSelected = true
            selectedSize = 3
        }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("selectedSize", selectedSize)
            putExtra("selectedAgainst", selectedAgainst)
            putExtra("selectedLevel", selectedLevel)

        }
        startActivity(intent)
    }

    private fun showLevelDialog() {
        val dialog = ChooseLevelDialog.newInstance(selectedSize, selectedAgainst)
        dialog.show(supportFragmentManager, ChooseLevelDialog.TAG)
    }

}