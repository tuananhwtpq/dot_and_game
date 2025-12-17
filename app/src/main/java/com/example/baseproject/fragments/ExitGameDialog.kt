package com.example.baseproject.fragments

import android.content.Intent
import com.example.baseproject.activities.StartActivity
import com.example.baseproject.bases.BaseDialogFragment
import com.example.baseproject.databinding.FragmentExitGameDialogBinding

class ExitGameDialog : BaseDialogFragment<FragmentExitGameDialogBinding>(
    FragmentExitGameDialogBinding::inflate
) {

    companion object {
        const val TAG = "ExitGameDialog"

        fun newInstance(): ExitGameDialog {
            return ExitGameDialog()
        }
    }

    override fun initView() {
    }

    override fun initActionView() {
        isCancelable = false
        marqueueText()
        handleButtonClick()
    }

    private fun handleButtonClick() {
        binding.btnExit.setOnClickListener {
            goToHome()
        }

        binding.btnStay.setOnClickListener {
            dismiss()
        }
    }

    private fun goToHome() {
        val intent = Intent(requireContext(), StartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun marqueueText() {
        binding.tvExitGame.isSelected = true
        binding.tvExit.isSelected = true
        binding.tvStay.isSelected = true
        binding.tvTop.isSelected = true
    }

}