package com.example.baseproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.baseproject.R
import com.example.baseproject.bases.BaseDialogFragment
import com.example.baseproject.databinding.FragmentExitGameDialogBinding

class ExitGameDialog : BaseDialogFragment<FragmentExitGameDialogBinding>(
    FragmentExitGameDialogBinding::inflate
) {
    override fun initView() {
    }

    override fun initActionView() {
        isCancelable = false
        marqueueText()
        handleButtonClick()
    }

    private fun handleButtonClick() {
        binding.btnExit.setOnClickListener {

        }

        binding.btnStay.setOnClickListener {
            dismiss()
        }
    }

    private fun marqueueText() {
        binding.tvExitGame.isSelected = true
        binding.tvExit.isSelected = true
        binding.tvStay.isSelected = true
        binding.tvTop.isSelected = true
    }

}