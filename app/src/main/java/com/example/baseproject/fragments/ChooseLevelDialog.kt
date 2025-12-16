package com.example.baseproject.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.baseproject.R
import com.example.baseproject.activities.MainActivity
import com.example.baseproject.bases.BaseDialogFragment
import com.example.baseproject.databinding.FragmentChooseLevelDialogBinding
import com.example.baseproject.utils.SharedPrefManager
import com.example.baseproject.utils.invisible
import com.example.baseproject.utils.setOnUnDoubleClick
import com.example.baseproject.utils.visible

class ChooseLevelDialog : BaseDialogFragment<FragmentChooseLevelDialogBinding>(
    FragmentChooseLevelDialogBinding::inflate
) {

    companion object {
        const val TAG = "ChooseLevelDialog"

        fun newInstance(selectedSize: Int, selectedAgainst: String): ChooseLevelDialog {

            val args = Bundle().apply {
                putInt("selectedSize", selectedSize)
                putString("selectedAgainst", selectedAgainst)
            }

            return ChooseLevelDialog().apply {
                arguments = args
            }
        }
    }

    private var currentLevel = ""
    private var currentSize = 1
    private var currentAgainst = ""

    override fun initView() {
        isCancelable = false
        currentSize = arguments?.getInt("selectedSize", 1) ?: 1
        currentAgainst = arguments?.getString("selectedAgainst", "computer") ?: "computer"
    }

    override fun initActionView() {
        handleButtonClick()
        marqueueText()
    }

    private fun marqueueText() {
        binding.tvEasy.isSelected = true
        binding.tvMedium.isSelected = true
        binding.tvHard.isSelected = true
        binding.tvPlay.isSelected = true
        binding.tvChooseLevel.isSelected = true
    }

    private fun handleButtonClick() {
        binding.btnExit.setOnClickListener {
            dismiss()
        }

        binding.btnEasy.setOnUnDoubleClick {
            binding.icDoneEasy.visible()
            binding.icDoneMedium.invisible()
            binding.icDoneHard.invisible()
            currentLevel = "easy"
        }

        binding.btnMedium.setOnUnDoubleClick {
            binding.icDoneEasy.invisible()
            binding.icDoneMedium.visible()
            binding.icDoneHard.invisible()
            currentLevel = "medium"
        }

        binding.btnHard.setOnUnDoubleClick {
            binding.icDoneEasy.invisible()
            binding.icDoneMedium.invisible()
            binding.icDoneHard.visible()
            currentLevel = "hard"
        }

        binding.btnPlay.setOnUnDoubleClick {
            goToMain()
        }
    }

    private fun goToMain() {
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            putExtra("selectedSize", currentSize)
            putExtra("selectedAgainst", currentAgainst)
            putExtra("selectedLevel", currentLevel)
        }
        startActivity(intent)
    }

}