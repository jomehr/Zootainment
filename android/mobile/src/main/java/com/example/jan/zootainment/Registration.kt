package com.example.jan.zootainment

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_registration.*

class Registration : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()

        register_registerButton.setOnClickListener(this)
        if (auth.currentUser?.isAnonymous!!){
            register_signIn.visibility = View.INVISIBLE
        }else {
            register_signIn.setOnClickListener(this)
        }
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        if (auth.currentUser?.isAnonymous!!) {
            auth.currentUser?.linkWithCredential(EmailAuthProvider.getCredential(email, password))
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "linkWithCredential:success")
                        val user = task.result?.user
                        sendEmailVerification(user!!)
                    } else {
                        Log.w(TAG, "linkWithCredential:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, send email verification and load MainActivity
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        sendEmailVerification(user!!)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
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

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
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
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.register_registerButton -> createAccount(register_emailEdit.text.toString(), register_passwordEdit.text.toString())
            R.id.register_signIn -> {
                startActivity(Intent(this, Login::class.java))
                finish()
            }
        }
    }

    companion object {
        private const val TAG = "Registration"
    }
}
