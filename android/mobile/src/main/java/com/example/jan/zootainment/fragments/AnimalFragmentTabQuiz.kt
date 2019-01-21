package com.example.jan.zootainment.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.example.jan.zootainment.QuizActivity
import com.example.jan.zootainment.R

class AnimalFragmentTabQuiz : Fragment() {

    private lateinit var quizButton: Button
    private lateinit var totalProgressBar: ProgressBar
    private lateinit var totalTextView: TextView
    private lateinit var progressTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        //init view
        val rootView = inflater.inflate(R.layout.content_animal_quiz, container, false)
        quizButton = rootView.findViewById(R.id.quiz_start_button)
        totalProgressBar = rootView.findViewById(R.id.question_total_pb)
        totalTextView = rootView.findViewById(R.id.question_total)
        progressTextView = rootView.findViewById(R.id.question_limit_progress)

        //set up view
        totalProgressBar.visibility = View.VISIBLE

        return rootView
    }

    fun putQuizArguments(args: Bundle) {
        totalProgressBar.visibility = View.GONE
        totalTextView.text = args.getInt("questions").toString()
        progressTextView.text = args.getInt("progress").toString()

        quizButton.setOnClickListener { loadQuiz(args) }

    }

    private fun loadQuiz(args: Bundle) {
        Log.d(TAG, "loading quiz")
        val intent = Intent(activity, QuizActivity::class.java)
        intent.putExtra("animal", args.getString("animal"))
        intent.putExtra("questions", args.getInt("questions"))
        startActivity(intent)
    }

    companion object {
        private const val TAG = "AnimalTabQuiz"
    }
}