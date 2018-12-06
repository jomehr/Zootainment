package com.example.jan.zootainment

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, send email verification and load MainActivity
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(baseContext,
                                    "Verification email sent to ${user.email} ",
                                    Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else {
                                Log.e(TAG, "sendEmailVerification", task.exception)
                                Toast.makeText(baseContext,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                        }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateForm(): Boolean {
        var valid = true

        //check if forms are empty
        val email = register_emailEdit.text.toString()
        if (TextUtils.isEmpty(email)) {
            register_emailEdit.error = "Required."
            valid = false
        } else {
            register_emailEdit.error = null
        }

        val password = register_passwordEdit.text.toString()
        if (TextUtils.isEmpty(password)) {
            register_passwordEdit.error = "Required."
            valid = false
        } else {
            register_passwordEdit.error = null
        }

        return valid
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.register_registerButton -> createAccount(
                register_emailEdit.text.toString(),
                register_passwordEdit.text.toString()
            )
            R.id.register_signIn -> startActivity(Intent(this, Login::class.java))
        }
    }

    companion object {
        private const val TAG = "Registration"
    }
}
