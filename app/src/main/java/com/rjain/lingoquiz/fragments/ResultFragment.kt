package com.rjain.lingoquiz.fragments

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rjain.lingoquiz.MainActivity
import com.rjain.lingoquiz.R
import com.rjain.lingoquiz.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private lateinit var binding : FragmentResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        MainActivity.currentFrag = ResultFragment()
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_result, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scored = "You've Scored ${MainActivity.score}"
        binding.resultTextview.text = scored
        binding.activityMainStartGameButton.setOnClickListener { findNavController().navigate(R.id.action_resultFragment_to_homeScreenFragment) }
        if(MainActivity.score == 0){
            binding.resultImageview.setImageDrawable(binding.root.resources.getDrawable(R.drawable.ic_sad))
        }

        scaleImage(binding.resultImageview)

        binding.resultImageview.setOnClickListener { scaleImage(binding.resultImageview) }

    }

    private fun scaleImage(resultImageview: ImageView) {
        val mediaPlayer = MediaPlayer.create(binding.root.context, R.raw.result)
        mediaPlayer.start()
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.5f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.5f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            resultImageview, scaleX, scaleY)
        animator.repeatCount = 3
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }

}