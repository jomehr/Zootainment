package com.example.jan.zootainment

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.jan.zootainment.data.Question
import com.example.jan.zootainment.util.ProximityContentUtils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_quiz_question.*
import kotlinx.android.synthetic.main.content_answers_rect.*

class QuizQuestion : AppCompatActivity(), View.OnClickListener {

    private val timeTotal: Long = 20 * 1000 //20 seconds
    private val interval: Long = 1000 //1 second
    private var finishedMillis: Long = 0
    private var progressTotal: Int = 0
    private var correctAnswer: Int = 0
    private var questionCounter: Int = 0

    private lateinit var timer: CountDownTimer
    private lateinit var animal: String
    private lateinit var questionReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)
        Log.d(TAG, "onCreate called...")

        //get data from previous activity
        animal = intent.getStringExtra("animal")

        //init database
        questionReference = FirebaseDatabase.getInstance().reference
            .child("enclosure_1").child("questions")

        //init views
        answer1.setOnClickListener(this)
        answer2.setOnClickListener(this)
        answer3.setOnClickListener(this)
        answer4.setOnClickListener(this)

        question_bg.setBackgroundColor(ContextCompat.getColor(this, ProximityContentUtils.getColor(animal)))
        question_ll.visibility = View.GONE
        question_pb.visibility = View.VISIBLE

        timer = timer(timeTotal, interval)
    }

    private fun loadQuestion() {
        questionCounter++
        val progressText = "$questionCounter/5"

        question_progress.text = progressText
        questionReference.child("q$questionCounter").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.d(TAG, "q$questionCounter")
                    val questionData = dataSnapshot.getValue(Question::class.java)

                    if (questionData == null) {
                        Log.e(TAG, "Question is unexpectedly null")
                        Toast.makeText(baseContext,
                            "Error: could not fetch question.",
                            Toast.LENGTH_SHORT).show()
                    }else {
                        Log.d(TAG, "Question loaded successfully")

                        correctAnswer = questionData.solution!!

                        question.text = questionData.question
                        answer1.text = questionData.answer1
                        answer2.text = questionData.answer2
                        answer3.text = questionData.answer3
                        answer4.text = questionData.answer4

                        question_pb.visibility = View.GONE
                        question_ll.visibility = View.VISIBLE

                        timer.start()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "loadQuestion:onCancelled", databaseError.toException())
                }
            }
        )
    }

    private fun timer (timeTotal: Long, interval: Long): CountDownTimer {
        return object: CountDownTimer(timeTotal, interval) {

            override fun onTick(millisUntilFinished: Long) {
                finishedMillis = timeTotal - millisUntilFinished
                progressTotal = ((finishedMillis.toDouble() / timeTotal.toDouble()) * 100.0).toInt()

                question_pb_timer.progress = progressTotal
            }

            override fun onFinish() {
                question_pb_timer.progress = 100
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

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart called...")
        loadQuestion()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called...")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause called...")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop called...")
    }

    override fun onClick(v: View) {
        timer.cancel()
        if (v.tag == correctAnswer.toString()) {
            v.background = getDrawable(R.drawable.shape_round_green)
            AlertDialog.Builder(this@QuizQuestion)
                .setTitle("Congratulations!")
                .setMessage("The answer was correct! You have received 250 points.")
                .setCancelable(false)
                .setPositiveButton("Continue") {dialog, id ->
                    loadQuestion()
                    v.background = getDrawable(R.drawable.shape_round)
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
            v.background = getDrawable(R.drawable.shape_round_wrong)
            AlertDialog.Builder(this@QuizQuestion)
                .setTitle("Ups!")
                .setMessage("The answer was wrong! Do you want to continue?")
                .setCancelable(false)
                .setPositiveButton("Continue") {dialog, id ->
                    loadQuestion()
                    v.background = getDrawable(R.drawable.shape_round)
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