package com.example.baseproject.fragments

import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.activities.IntroActivity
import com.example.baseproject.bases.BaseFragment
import com.example.baseproject.databinding.FragmentIntroBinding

class IntroFragment : BaseFragment<FragmentIntroBinding>(FragmentIntroBinding::inflate) {

    private val ARG_OBJECT = "position"

    private val position by lazy {
        requireArguments().getInt(ARG_OBJECT)
    }

    override fun initData() {}

    override fun initView() {
        if (arguments != null) {
            fragmentPosition(requireArguments().getInt(ARG_OBJECT))
        }
    }

    override fun initActionView() {
        binding.btnNext2.setOnClickListener {
            (activity as? IntroActivity)?.nextPage()
        }

        binding.btnNext1.setOnClickListener {
            (activity as? IntroActivity)?.nextPage()
        }

        binding.tvBig.isSelected = true
        binding.tvSmall.isSelected = true
    }

    private fun fragmentPosition(position: Int) {
        when (position) {
            0 -> {
                Glide.with(this).load(R.drawable.bg_intro_1).into(binding.ivIntro)
                binding.tvBig.text = getString(R.string.ready_for_dot_battle)
                binding.tvSmall.text =
                    getString(R.string.friends_or_ai_which_challenge_will_you_choose)
                binding.btnNext1.text = getString(R.string.next)
                binding.btnNext2.text = getString(R.string.next)
                binding.dotIndicator1.setImageResource(R.drawable.dot_1)
                binding.dotIndicator2.setImageResource(R.drawable.dot_1)
            }
            1 -> {
                Glide.with(this).load(R.drawable.bg_intro_2).into(binding.ivIntro)
                binding.tvBig.text = getString(R.string.master_boxes_crush_rivals)
                binding.tvSmall.text = getString(R.string.complete_boxes_and_defeat_every_opponent)
                binding.btnNext1.text = getString(R.string.next)
                binding.btnNext2.text = getString(R.string.next)
                binding.dotIndicator1.setImageResource(R.drawable.dot_2)
                binding.dotIndicator2.setImageResource(R.drawable.dot_2)
            }
            else -> {
                Glide.with(this).load(R.drawable.bg_intro_3).into(binding.ivIntro)
                binding.tvBig.text = getString(R.string.play_smart_win_big)
                binding.tvSmall.text = getString(R.string.fill_the_board_and_become_the_winner)
                binding.btnNext1.text = getString(R.string.get_started)
                binding.btnNext2.text = getString(R.string.get_started)
                binding.dotIndicator1.setImageResource(R.drawable.dot_3)
                binding.dotIndicator2.setImageResource(R.drawable.dot_3)
            }
        }
    }

}