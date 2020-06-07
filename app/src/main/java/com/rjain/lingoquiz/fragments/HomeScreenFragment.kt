package com.rjain.lingoquiz.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.rjain.lingoquiz.MainActivity
import com.rjain.lingoquiz.R
import com.rjain.lingoquiz.databinding.FragmentHomeScreenBinding

class HomeScreenFragment : Fragment() {
    private lateinit var binding: FragmentHomeScreenBinding
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        MainActivity.score = 0
        MainActivity.quesNo = 0
        if(mAuth.currentUser == null){
            findNavController().navigate(R.id.action_homeScreenFragment_to_loginFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home_screen,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.currentFrag = HomeScreenFragment()
        binding.fragmentHomescreenStartGameButton.setOnClickListener{
            val mediaPlayer = MediaPlayer.create(binding.root.context, R.raw.start)
            mediaPlayer.start()
            findNavController().navigate(R.id.action_homeScreenFragment_to_quizFragment)
        }

    }
}