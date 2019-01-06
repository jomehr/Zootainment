package com.example.jan.zootainment.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.jan.zootainment.QuizActivity
import com.example.jan.zootainment.R

class AnimalFragmentTabQuiz: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        //init view
        val rootView: View = inflater.inflate(R.layout.content_animal_quiz, container, false)
        val button = rootView.findViewById<Button>(R.id.quiz_start_button)
        val questionCounter = rootView.findViewById<TextView>(R.id.question_total)

        //load data
        val intent: String = arguments?.getString("animal")!!

        //set up view
        button.setOnClickListener { loadQuiz(intent) }
        questionCounter.text = arguments?.getString("questions")

        return rootView
    }

    private fun loadQuiz(animal: String) {
        val intent = Intent(activity, QuizActivity::class.java)
        intent.putExtra("animal", animal)
        startActivity(intent)
    }
}