package com.example.domd.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.domd.R
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val KEY_TITLE = "username"
    private val KEY_DESCRIPTION = "password"

    private lateinit var buttonLogin: Button
    private lateinit var buttonGuestLogin: Button
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    
    private var db = FirebaseFirestore.getInstance()
    private var infoRef = db.collection("secretStuff").document()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonLogin = findViewById<Button>(R.id.button)
        buttonGuestLogin = findViewById<Button>(R.id.guest)
        editTextUsername = findViewById<EditText>(R.id.usrText)
        editTextPassword = findViewById<EditText>(R.id.pswText)

        createWidgets()
    }

    private fun createWidgets() {
        val loginText = "Log in"
        val loginGuestText = "Log in as guest"
        buttonLogin.text = loginText
        buttonGuestLogin.text = loginGuestText

        editTextUsername.hint = "Username"
        editTextPassword.hint = "Password"

        buttonLogin.setOnClickListener {
            login(editTextUsername.text.toString(), editTextPassword.text.toString())
        }
        buttonGuestLogin.setOnClickListener {
            val intent = Intent(this, MainPage::class.java)
            intent.putExtra("guest", true)
            startActivity(intent)
        }
    }

    private fun login(username: String, password: String) {
        val info = mapOf (
            KEY_TITLE to username,
            KEY_DESCRIPTION to password
        )

        infoRef.set(info)
            .addOnSuccessListener { Log.d(TAG, "Successful!") }
            .addOnFailureListener { e -> Log.w(TAG, "error: $e") }

        if (password != "") {
            val intent = Intent(this, MainPage::class.java)
            intent.putExtra("usr", username)
            intent.putExtra("guest", false)
            startActivity(intent)
        }
        else {
            // Password is wrong, show error message
            val errorMessage = findViewById<TextView>(R.id.errorMessage)
            val errorMessageText = "Incorrect password. Please try again."
            errorMessage.text = errorMessageText
            errorMessage.visibility = View.VISIBLE
        }
    }
}