package com.example.domd

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainPage : AppCompatActivity() {
    private lateinit var domd: TextView;
    private lateinit var user: TextView;
    private lateinit var createGame: Button;
    private var username = "";
    private var guest: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        createWidgets()
    }

    private fun createWidgets() {
        val intent = intent
        guest = intent.getBooleanExtra("guest", false)
        username = intent.getStringExtra("usr").toString()

        domd = findViewById(R.id.domdText)
        user = findViewById(R.id.user)
        createGame = findViewById(R.id.createGameBtn)

        domd.text = "DÖMD"

        if (guest) {
            user.text = "Welcome guest!"
        }
        else {
            user.text = "Welcome $username!"
        }
        createGame.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Alert Title")
        alertDialogBuilder.setMessage("Alert message goes here.")

        // Set a positive button and its click listener
        alertDialogBuilder.setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
            // Perform an action when the "OK" button is clicked
            // This can be left empty or used for some action
            dialog.dismiss()
        }

        // Create and show the alert dialog
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}