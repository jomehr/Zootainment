package com.example.jan.zootainment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import com.example.jan.zootainment.util.ProximityContentUtils
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_controler.*

class Controller : AppCompatActivity() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    private lateinit var animal: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controler)

        //get data from previous activity
        animal = intent.getStringExtra("animal")

        //init view
        controller_background.setBackgroundColor(ContextCompat.getColor(this, ProximityContentUtils.getColor(animal)))
        controller_info.text = "You now have direct control over the feeding cannon in the $animal enclosure"

        //init data
        val databaseRef: DatabaseReference = database.child(animal).child("devices").child("cannon_1")

        action_moveLeft.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    databaseRef.child("move_left").setValue(true)
                }
                MotionEvent.ACTION_UP -> {
                    databaseRef.child("move_left").setValue(false)
                }
            }
            true
        }

        action_moveRight.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    databaseRef.child("move_right").setValue(true)
                }
                MotionEvent.ACTION_UP -> {
                    databaseRef.child("move_right").setValue(false)
                }
            }
            true
        }

        action_shoot.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    databaseRef.child("shoot").setValue(true)
                }
                MotionEvent.ACTION_UP -> {
                    databaseRef.child("shoot").setValue(false)
                }
            }
            true
        }
    }
}