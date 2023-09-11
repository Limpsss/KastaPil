package com.example.domd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var btn: Button
    private lateinit var guest: Button
    private lateinit var username: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createWidgets()
    }

    private fun createWidgets() {
        btn = findViewById<Button>(R.id.button)
        guest = findViewById<Button>(R.id.guest)
        username = findViewById<EditText>(R.id.usrText)
        password = findViewById<EditText>(R.id.pswText)

        btn.text = "Log in"
        guest.text = "Log in as guest"
        username.hint = "Username"
        password.hint = "Password"

        btn.setOnClickListener {
            logIn(username.text.toString(), password.text.toString())
        }
        guest.setOnClickListener {
            var intent = Intent(this, MainPage::class.java)
            intent.putExtra("guest", true)
            startActivity(intent)
        }
    }

    private fun logIn(username: String, password: String) {
        val correctPassword = "bajs"
        var guest: Boolean = false

        if (password == correctPassword) {
            val intent = Intent(this, MainPage::class.java)
            intent.putExtra("usr", username)
            intent.putExtra("guest", guest)
            startActivity(intent)
        }
        else {
            // Password is wrong, show error message
            val errorMessage = findViewById<TextView>(R.id.errorMessage)
            errorMessage.text = "Incorrect password. Please try again."
            errorMessage.visibility = View.VISIBLE
        }
    }
}