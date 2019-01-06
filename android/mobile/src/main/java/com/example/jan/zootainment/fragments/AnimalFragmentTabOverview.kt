package com.example.jan.zootainment.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.jan.zootainment.Controller
import com.example.jan.zootainment.R

class AnimalFragmentTabOverview: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        //init view
        val rootView: View = inflater.inflate(R.layout.content_animal_overview, container, false)
        val limit = rootView.findViewById<TextView>(R.id.feeding_limit)
        val cost = rootView.findViewById<TextView>(R.id.feeding_cost)
        val feedButton = rootView.findViewById<Button>(R.id.feed_button)

        //set up view
        limit.text = arguments?.getString("limit")
        cost.text = arguments?.getString("cost")
        feedButton.setOnClickListener { startActivity(Intent(activity, Controller::class.java)) }

        return rootView
    }
}