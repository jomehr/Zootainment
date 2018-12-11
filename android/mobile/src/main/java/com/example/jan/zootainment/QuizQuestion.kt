package com.example.jan.zootainment

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.widget.ProgressBar
import com.example.jan.zootainment.util.ProximityContentUtils
import kotlinx.android.synthetic.main.activity_quiz_question.*

class QuizQuestion : AppCompatActivity() {

    private val time :Long = 45 * 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)

        val animal: String = intent.getStringExtra("animal")
        question_bg.setBackgroundColor(ProximityContentUtils.getColor(animal))

        val pb = question_pb.findViewById<ProgressBar>(R.id.question_pb)

        object :CountDownTimer(time, 1000) {

            override fun onFinish() {
                pb.progress = 100
                loadNextQuestion(animal)
            }

            override fun onTick(millisUntilFinished: Long) {
                val finishedMillis :Long = time - millisUntilFinished
                val total :Int = ((finishedMillis.toDouble() / time.toDouble()) * 100.0).toInt()

                pb.progress = total
            }
        }.start()
    }

    private fun loadNextQuestion(animal :String) {

    }
}