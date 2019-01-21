package com.example.jan.zootainment

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser

    // Check if user is signed in (non-null)
    //TODO preload data useful?
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (user != null) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        } else {
            signInAnonymously()
        }
    }

    private fun signInAnonymously() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, load MainActivity
                    Log.d(TAG, "signInAnonymously:success")
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInAnonymously:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        private const val TAG = "SplashScreen"
    }
}