package com.example.jan.zootainment

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.example.jan.zootainment.util.ProximityContentUtils
import kotlinx.android.synthetic.main.activity_quiz_question.*

class QuizQuestion : AppCompatActivity(), View.OnClickListener {

    private val timeTotal: Long = 20 * 1000 //45 seconds
    private val interval: Long = 1000 //1 second
    private var finishedMillis: Long = 0
    private var progressTotal: Int = 0
    private var correctAnswer: Int = 0

    private lateinit var pb: ProgressBar
    private lateinit var timer: CountDownTimer
    private lateinit var animal: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)
        Log.d(TAG, "onCreate called...")

        correctAnswer = 4

        answer1.setOnClickListener(this)
        answer2.setOnClickListener(this)
        answer3.setOnClickListener(this)
        answer4.setOnClickListener(this)

        animal = intent.getStringExtra("animal")
        question_bg.setBackgroundColor(ProximityContentUtils.getColor(animal))

        pb = findViewById(R.id.question_pb)
    }

    private fun loadQuestion() {
        //TODO load data from persistent storage (cloud or local)
        timer.cancel()
        question.text = "How much food eats an elephant per day?"
        answer1.text = "75kg"
        answer2.text = "100kg"
        answer3.text = "150kg"
        answer4.text = "200kg"
        correctAnswer = 3
        timer.start()
    }



    private fun timer (timeTotal: Long, interval: Long): CountDownTimer {
        return object: CountDownTimer(timeTotal, interval) {

            override fun onTick(millisUntilFinished: Long) {
                finishedMillis = timeTotal - millisUntilFinished
                progressTotal = ((finishedMillis.toDouble() / timeTotal.toDouble()) * 100.0).toInt()

                pb.progress = progressTotal
            }

            override fun onFinish() {
                pb.progress = 100
                AlertDialog.Builder(this@QuizQuestion)
                    .setTitle("Ups!")
                    .setMessage("No answer was selected. Do you want to continue?")
                    .setCancelable(false)
                    .setPositiveButton("Continue") {dialog, id ->
                        loadQuestion()
                        dialog.cancel() }
                    .setNegativeButton("Stop") {dialog, id ->
                        startActivity(Intent(this@QuizQuestion, MainActivity::class.java))
                        dialog.cancel()
                        finish()}
                    .setNeutralButton("Spend points") {dialog, id ->
                        startActivity(Intent(this@QuizQuestion, Controller::class.java))
                        dialog.cancel()
                        finish()}
                    .show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called...")
        timer = timer(timeTotal, interval).start()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause called...")
        timer.cancel()
    }

    override fun onClick(v: View) {
        timer.cancel()
        if (v.tag == correctAnswer.toString()) {
            v.setBackgroundColor(resources.getColor(R.color.correct))
            AlertDialog.Builder(this@QuizQuestion)
                .setTitle("Congratulations!")
                .setMessage("The answer was correct! You have received 250 points.")
                .setCancelable(false)
                .setPositiveButton("Continue") {dialog, id ->
                    loadQuestion()
                    v.setBackgroundColor(resources.getColor(R.color.white))
                    dialog.cancel()}
                .setNegativeButton("Stop") {dialog, id ->
                    startActivity(Intent(this@QuizQuestion, MainActivity::class.java))
                    dialog.cancel()
                    finish()}
                .setNeutralButton("Spend points") {dialog, id ->
                    startActivity(Intent(this@QuizQuestion, Controller::class.java))
                    dialog.cancel()
                    finish()}
                .show()
        }
        else {
            v.setBackgroundColor(resources.getColor(R.color.wrong))
            AlertDialog.Builder(this@QuizQuestion)
                .setTitle("Ups!")
                .setMessage("The answer was wrong! Do you want to continue?")
                .setCancelable(false)
                .setPositiveButton("Continue") {dialog, id ->
                    loadQuestion()
                    v.setBackgroundColor(resources.getColor(R.color.white))
                    dialog.cancel() }
                .setNegativeButton("Stop") {dialog, id ->
                    startActivity(Intent(this@QuizQuestion, MainActivity::class.java))
                    dialog.cancel()
                    finish()}
                .setNeutralButton("Spend points") {dialog, id ->
                    startActivity(Intent(this@QuizQuestion, Controller::class.java))
                    dialog.cancel()
                    finish()}
                .show()
        }
    }

    companion object {
        private const val TAG = "QuizQuestion"
    }
}