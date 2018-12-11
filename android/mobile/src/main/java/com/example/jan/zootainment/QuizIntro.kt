package com.example.jan.zootainment

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.jan.zootainment.util.ProximityContentUtils
import kotlinx.android.synthetic.main.activity_quiz_intro.*

class QuizIntro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_intro)

        val animal: String = intent.getStringExtra("animal")
        quiz_introBackground.setBackgroundColor(ProximityContentUtils.getColor(animal))

        quiz_introReady.setOnClickListener{
            loadQuestion(animal)
        }
    }

    private fun loadQuestion(animal: String) {
        val intent = Intent(this@QuizIntro, QuizQuestion::class.java)
        intent.putExtra("animal", animal)
        startActivity(intent)
        finish()
    }
}