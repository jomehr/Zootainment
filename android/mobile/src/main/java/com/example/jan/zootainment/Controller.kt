package com.example.jan.zootainment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.jan.zootainment.util.ProximityContentUtils
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_controler.*

class Controller : AppCompatActivity() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val timeWarning: Long = 60 * 1000 //60 seconds

    private lateinit var databaseRef: DatabaseReference
    private lateinit var handler: Handler
    private lateinit var animal: String
    private lateinit var device: String
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controler)

        //get data from previous activity
        animal = intent.getStringExtra("animal")
        device = intent.getStringExtra("device")



        //init view
        controller_background.setBackgroundColor(ContextCompat.getColor(this, ProximityContentUtils.getColor(animal)))
        controller_info.text = "You now have direct control over the ${device.split("_")[0]} in the $animal enclosure"

        //init data
        databaseRef = database.child(animal).child("devices").child(device)
        databaseRef.child("active").setValue(true)


        when (device) {
            "cannon_1" -> {
                initCannonControl()
            }
            "sprinkler_1" -> {
                initSprinklerControl()
            }
        }
    }

    private fun initCannonControl() {
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
        action_shoot.setOnClickListener {
            databaseRef.child("shoot").setValue(true)
            Handler().postDelayed({
                databaseRef.child("shoot").setValue(false)
                databaseRef.child("active").setValue(false)
                Toast.makeText(this, "Good shot! The $animal thank you for the snack.", Toast.LENGTH_LONG).show()
                finish()
            }, 3000)
        }
    }

    private fun initSprinklerControl() {
        action_moveLeft.visibility = View.GONE
        action_moveRight.visibility = View.GONE
        action_shoot.setOnClickListener {
            databaseRef.child("shoot").setValue(true)
            Handler().postDelayed({
                databaseRef.child("shoot").setValue(false)
                databaseRef.child("active").setValue(false)
                Toast.makeText(this, "Good shot! The $animal thank you for the refreshment.", Toast.LENGTH_LONG).show()
                finish()
            }, 6000)
        }
    }

    override fun onStart() {
        super.onStart()
        handler = Handler()
        handler.postDelayed({
            vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(300)
            Toast.makeText(
                this,
                "Warning! Please do not delay. Others are waiting. 30 seconds remaining",
                Toast.LENGTH_LONG
            ).show()
            handler.postDelayed({
                Toast.makeText(this, "Sorry! Your time is over. You can try again later", Toast.LENGTH_LONG).show()
                vibrator.vibrate(600)
                databaseRef.child("active").setValue(false)
                finish()
            }, timeWarning)
        }, timeWarning)
    }

    override fun onStop() {
        Log.d("Controller", "onStop")
        super.onStop()
        handler.removeCallbacksAndMessages(null)
    }
}