package com.example.jan.zootainment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_controler.*

class Controller : AppCompatActivity() {

    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        .child("enclosure_1").child("devices")
        .child("cannon_1").child("movement_left")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controler)

        action_moveLeft.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.background.setTint(ContextCompat.getColor(this, R.color.colorPrimary))
                    databaseRef.setValue(true)
                }
                MotionEvent.ACTION_UP -> {
                    v.background.setTint(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                    databaseRef.setValue(false)
                }
            }
            true
        }

        action_moveRight.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.background.setTint(ContextCompat.getColor(this, R.color.colorPrimary))
                    databaseRef.setValue(true)
                }
                MotionEvent.ACTION_UP -> {
                    v.background.setTint(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                    databaseRef.setValue(false)
                }
            }
            true
        }

        action_shoot.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.background.setTint(ContextCompat.getColor(this, R.color.colorPrimary))
                    databaseRef.setValue(true)
                }
                MotionEvent.ACTION_UP -> {
                    v.background.setTint(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                    databaseRef.setValue(false)
                }
            }
            true
        }
    }
}