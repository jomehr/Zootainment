package com.example.jan.zootainment.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.jan.zootainment.R

class AnimalFragmentTabOverview: Fragment(), PointCounter.OnPointsCounterListener {

    private lateinit var mListener: OnFragmentInteractionListener
    private lateinit var animal: String

    private var curCount = 0

    override fun messageFromCounter(count: Int) {
        Log.d(TAG, "counter: $count")
        curCount = count
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        //init view
        val rootView: View = inflater.inflate(R.layout.content_animal_overview, container, false)
        val costText = rootView.findViewById<TextView>(R.id.feeding_cost)
        val limitText = rootView.findViewById<TextView>(R.id.feeding_limit)
        val feedButton = rootView.findViewById<Button>(R.id.feed_button)

        val animal = arguments?.getString("animal")!!
        val cost =  arguments?.getInt("cost")
        val limit = arguments?.getString("limit")

        //set up view
        costText.text = cost.toString()
        limitText.text = limit
        feedButton.setOnClickListener {
            AlertDialog.Builder(context!!)
                .setTitle("Warning")
                .setMessage("You are about to spend ${cost.toString()} points. Your total points will drop to ${(curCount - cost!!)}")
                .setCancelable(false)
                .setPositiveButton("Continue") {dialog, id ->
                    val counter: PointCounter = childFragmentManager.findFragmentById(R.id.fragment_point_counter) as PointCounter
                    counter.startCounterDown(curCount, cost, animal)
                    dialog.cancel() }
                .setNegativeButton("Stop") {dialog, id ->
                    dialog.cancel() }
                .show()
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val pointCounter = PointCounter()
        childFragmentManager.beginTransaction().replace(R.id.fragment_point_counter, pointCounter).commit()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) mListener = context
        else throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
    }

    interface OnFragmentInteractionListener {
        fun messageFromParent()
    }

    companion object {
        private const val TAG = "AnimalTabOverview"
    }
}