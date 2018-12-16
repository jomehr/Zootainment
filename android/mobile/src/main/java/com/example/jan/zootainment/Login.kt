package com.example.jan.zootainment

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        login_anonButton.setOnClickListener(this)
        login_loginButton.setOnClickListener(this)
        login_register.setOnClickListener(this)
        login_resetpw.setOnClickListener(this)

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null)
        if (auth.currentUser != null) {
            Log.d(TAG, "user is anonymous: ${auth.currentUser?.uid}")
            startActivity(Intent(this@Login, MainActivity::class.java))
            finish()
        }
    }

    private fun signInAnonymously() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, load MainActivity
                    Log.d(TAG, "signInAnonymously:success")
                    startActivity(Intent(this@Login, MainActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInAnonymously:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, load MainActivity
                    Log.d(TAG, "signInWithEmail:success")
                    startActivity(Intent(this@Login, MainActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateForm(): Boolean {
        var valid = true

        //check if forms are empty
        val email = login_emailEdit.text.toString()
        if (TextUtils.isEmpty(email)) {
            login_emailEdit.error = "Required."
            valid = false
        } else {
            login_emailEdit.error = null
        }

        val password = login_passwordEdit.text.toString()
        if (TextUtils.isEmpty(password)) {
            login_passwordEdit.error = "Required."
            valid = false
        } else {
            login_passwordEdit.error = null
        }

        return valid
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.login_anonButton -> signInAnonymously()
            R.id.login_loginButton -> signIn(login_emailEdit.text.toString(), login_passwordEdit.text.toString())
            R.id.login_register -> {
                startActivity(Intent(this@Login, Registration::class.java))
                finish()
            }
        }
    }

    companion object {
        private const val TAG = "Login"
    }
}