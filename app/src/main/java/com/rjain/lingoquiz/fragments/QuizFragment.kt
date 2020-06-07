
package com.rjain.lingoquiz.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.*
import com.rjain.lingoquiz.MainActivity
import com.rjain.lingoquiz.R
import com.rjain.lingoquiz.databinding.FragmentQuizBinding
import com.rjain.lingoquiz.model.QuizModel

class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding
    private lateinit var quizModels : MutableList<QuizModel>
    private lateinit var quizdataRef : DatabaseReference
    private var mediaPlayer : MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_quiz,container,false)
        MainActivity.currentFrag = QuizFragment()
        binding.fragmentQuizRadioGroup.visibility = View.INVISIBLE
        quizdataRef = FirebaseDatabase.getInstance().reference
        quizModels = ArrayList()
        quizdataRef.child("quiz_data").addValueEventListener(object : ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {
                showToast(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                quizModels.clear()
                for(datasnapshot in p0.children){
                    val quizModel = datasnapshot.getValue(QuizModel::class.java) as QuizModel
                    quizModels.add(quizModel)
                }
                loadQuestions(quizModels as ArrayList<QuizModel>,MainActivity.quesNo)
                binding.fragmentQuizProgressBar.max = quizModels.size
                binding.fragmentQuizProgressBar.progress = MainActivity.quesNo
            }

        })

        if(MainActivity.quesNo > 0){
            binding.fragmentQuizQuestion.visibility = View.INVISIBLE
        }

        return binding.root
    }

    private fun loadQuestions(quizModels: ArrayList<QuizModel>, pos : Int) {
        val ques = quizModels[pos].ques
        val a = quizModels[pos].a
        val b = quizModels[pos].b
        val c = quizModels[pos].c
        val d = quizModels[pos].d
        val ans = quizModels[pos].ans
        binding.fragmentQuizRadioGroup.visibility = View.VISIBLE
        binding.fragmentQuizQuestion.visibility = View.VISIBLE
        binding.fragmentQuizOptionA.text = a
        binding.fragmentQuizOptionB.text = b
        binding.fragmentQuizOptionC.text = c
        binding.fragmentQuizOptionD.text = d
        binding.fragmentQuizQuestion.text = ques
        updateRadioButtonOnUserClick(ans)
    }

    private fun updateRadioButtonOnUserClick(corrAns : String) {
        binding.fragmentQuizRadioGroup.setOnCheckedChangeListener { _, checkedId ->

            val radio: RadioButton = binding.root.findViewById(checkedId)
            when (checkedId) {
                R.id.fragment_quiz_optionA ->
                    updateRadioButtonBackground(corrAns, radio)
                R.id.fragment_quiz_optionB ->
                    updateRadioButtonBackground(corrAns, radio)
                R.id.fragment_quiz_optionC ->
                    updateRadioButtonBackground(corrAns, radio)
                R.id.fragment_quiz_optionD ->
                    updateRadioButtonBackground(corrAns, radio)
            }
        }

    }

    private fun updateRadioButtonBackground(corrAns: String, radio : RadioButton){
        if(radio.isChecked) {
            MainActivity.quesNo += 1
            binding.fragmentQuizProgressBar.progress = MainActivity.quesNo
            if (corrAns == radio.text) {
                radio.setBackgroundResource(R.drawable.pass_background)
                MainActivity.score += 10
                mediaPlayer = MediaPlayer.create(binding.root.context,R.raw.correct)
                mediaPlayer?.setOnPreparedListener {
                    mediaPlayer?.start()
                }
            } else {
                mediaPlayer = MediaPlayer.create(binding.root.context,R.raw.fail)
                mediaPlayer?.setOnPreparedListener {
                    mediaPlayer?.start()
                }
                radio.setBackgroundResource(R.drawable.fail_background)
                showToast("Correct Answer: $corrAns")
            }
            Handler().postDelayed({
                if(MainActivity.quesNo == quizModels.size ){
                    findNavController().navigate(R.id.action_quizFragment_to_resultFragment)
                } else {
                    findNavController().navigate(R.id.action_quizFragment_to_quizFragment)
                    loadQuestions(quizModels as ArrayList<QuizModel>, MainActivity.quesNo)
                }
            }, 1000)
        }
    }

    private fun showToast(msg : String){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show()
    }
}