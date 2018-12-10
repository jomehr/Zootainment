package com.example.jan.zootainment

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_controler.*
import kotlinx.android.synthetic.main.app_bar_main.*

class Controller : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controler)

        database = FirebaseDatabase.getInstance().reference

        action_moveLeft.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> database.child("cannon_actions").child("movement_left").setValue(true)
                MotionEvent.ACTION_UP -> database.child("cannon_actions").child("movement_left").setValue(false)
            }
            true
        }

        action_moveRight.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> database.child("cannon_actions").child("movement_right").setValue(true)
                MotionEvent.ACTION_UP -> database.child("cannon_actions").child("movement_right").setValue(false)
            }
            true
        }

        action_shoot.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> database.child("cannon_actions").child("shoot").setValue(true)
                MotionEvent.ACTION_UP -> database.child("cannon_actions").child("shoot").setValue(false)
            }
            true
        }
    }
}