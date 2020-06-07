package com.rjain.lingoquiz

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.rjain.lingoquiz.databinding.ActivityMainBinding
import com.rjain.lingoquiz.fragments.QuizFragment
import com.rjain.lingoquiz.fragments.ResultFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity,R.layout.activity_main)
    }

    override fun onBackPressed() {
        if(currentFrag is QuizFragment || currentFrag is ResultFragment){
           displayDialog();
        } else { super.onBackPressed() }
    }

    private fun displayDialog() {
        val dialog = Dialog(this);
        dialog.setContentView(R.layout.exit_dialog)

        val quitBtn : Button = dialog.findViewById(R.id.yes_btn)
        val cancelBtn : Button = dialog.findViewById(R.id.cancel_btn)

        quitBtn.setOnClickListener { this.finish() }
        cancelBtn.setOnClickListener { dialog.dismiss() }

        dialog.show()
        val window : Window = dialog.window!!
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
    }

    companion object{
        lateinit var currentFrag : Fragment
        var quesNo : Int = 0
        var score : Int = 0
    }
}