package com.example.baseproject.activities

import android.view.View
import com.example.baseproject.bases.BaseActivity
import com.example.baseproject.databinding.ActivityMainBinding
import com.example.baseproject.utils.gone

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val selectedSize by lazy {
        intent.getIntExtra("selectedSize", 1)
    }

    private val selectedAgainst by lazy {
        intent.getStringExtra("selectedAgainst")
    }

    private val selectedLevel by lazy {
        intent.getStringExtra("selectedLevel")
    }


    override fun initData() {
        binding.tvSize.text = selectedSize.toString()
        binding.tvAgainst.text = selectedAgainst
        selectedLevel?.let {
            if (it.isNotEmpty()){
                binding.tvLevel.text = it
            } else {
                binding.tvLevel.gone()
            }
        }
    }

    override fun initView() {

    }

    override fun initActionView() {

    }


}