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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_quiz_question.*
import kotlinx.android.synthetic.main.content_answers_rect.*

class QuizActivity : AppCompatActivity(), View.OnClickListener {

    private val timeTotal: Long = 30 * 1000 //30 seconds
    private val interval: Long = 1000 //1 second
    private var finishedMillis: Long = 0
    private var progressTotal: Int = 0
    private var correctAnswer: Int = 0
    private var questionCounter: Int = 0

    private val firebase = FirebaseDatabase.getInstance().reference
    private val user = FirebaseAuth.getInstance().currentUser?.uid!!

    private lateinit var timer: CountDownTimer
    private lateinit var animal: String
    private lateinit var questionReference: DatabaseReference
    private lateinit var userReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)
        Log.d(TAG, "onCreate:UID $user")

        //get data from previous activity
        animal = intent.getStringExtra("animal")

        //init database
        questionReference = firebase.child(animal).child("questions")
        userReference = firebase.child("users").child(user)

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

        if (questionCounter < 4) {
            questionCounter++

            question_progress.text = "$questionCounter/4"

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

                    override fun onCancelled(error: DatabaseError) {
                        Log.w(TAG, "loadQuestion:onCancelled", error.toException())
                    }
                }
            )
        } else {
            Toast.makeText(baseContext,
                "You have reached the daily limit in this enclosure. Go to the next one.",
                Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@QuizActivity, MainActivity::class.java))
            finish()
        }

    }

    private fun calculatePoints(): Int {
        //TODO testing
        val points = ((timeTotal - finishedMillis)/1000).toInt()

        userReference.child("points").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "loadPoints:onCancelled", error.toException())
                }

                override fun onDataChange(data: DataSnapshot) {
                    val curPoints = data.getValue(Int::class.java)

                    val totalPoints = curPoints?.plus(points)
                    userReference.child("points").setValue(totalPoints)
                }
            }
        )

        return points
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
                updateQuestionFeedback(R.drawable.shape_round_wrong)
                //TODO make alertdialog builder util to clean up code
                AlertDialog.Builder(this@QuizActivity)
                    .setTitle("Ups!")
                    .setMessage("No answer was selected. Do you want to continue?")
                    .setCancelable(false)
                    .setPositiveButton("Continue") {dialog, id ->
                        loadQuestion()
                        dialog.cancel() }
                    .setNegativeButton("Stop") {dialog, id ->
                        startActivity(Intent(this@QuizActivity, MainActivity::class.java))
                        dialog.cancel()
                        finish()}
                    .setNeutralButton("Spend points") {dialog, id ->
                        val intent = Intent(this@QuizActivity, Controller::class.java)
                        intent.putExtra("animal", animal)
                        startActivity(intent)
                        dialog.cancel()
                        finish()}
                    .show()
            }
        }
    }

    private fun updateQuestionFeedback(drawable: Int) {
        when (questionCounter) {
            1 -> question_feedback1.background = getDrawable(drawable)
            2 -> question_feedback2.background = getDrawable(drawable)
            3 -> question_feedback3.background = getDrawable(drawable)
            4 -> question_feedback4.background = getDrawable(drawable)
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
            val points = calculatePoints()
            updateQuestionFeedback(R.drawable.shape_round_green)
            v.background = getDrawable(R.drawable.shape_round_green)

            AlertDialog.Builder(this@QuizActivity)
                .setTitle("Congratulations!")
                .setMessage("The answer was correct! You have received $points points.")
                .setCancelable(false)
                .setPositiveButton("Continue") {dialog, id ->
                    loadQuestion()
                    v.background = getDrawable(R.drawable.shape_round)
                    dialog.cancel()}
                .setNegativeButton("Stop") {dialog, id ->
                    startActivity(Intent(this@QuizActivity, MainActivity::class.java))
                    dialog.cancel()
                    finish()}
                .setNeutralButton("Spend points") {dialog, id ->
                    startActivity(Intent(this@QuizActivity, Controller::class.java))
                    dialog.cancel()
                    finish()}
                .show()
        }
        else {
            v.background = getDrawable(R.drawable.shape_round_wrong)
            updateQuestionFeedback(R.drawable.shape_round_wrong)

            AlertDialog.Builder(this@QuizActivity)
                .setTitle("Ups!")
                .setMessage("The answer was wrong! Do you want to continue?")
                .setCancelable(false)
                .setPositiveButton("Continue") {dialog, id ->
                    loadQuestion()
                    v.background = getDrawable(R.drawable.shape_round)
                    dialog.cancel() }
                .setNegativeButton("Stop") {dialog, id ->
                    startActivity(Intent(this@QuizActivity, MainActivity::class.java))
                    dialog.cancel()
                    finish()}
                .setNeutralButton("Spend points") {dialog, id ->
                    startActivity(Intent(this@QuizActivity, Controller::class.java))
                    dialog.cancel()
                    finish()}
                .show()
        }
    }

    companion object {
        private const val TAG = "QuizActivity"
    }
}