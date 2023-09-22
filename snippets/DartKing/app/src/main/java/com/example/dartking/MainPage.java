package com.example.dartking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainPage extends AppCompatActivity {
    private TextView domd;
    private TextView user;
    private Button createGame;
    private String username = "";
    private boolean guest = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        createWidgets();
    }

    private void createWidgets() {
        Intent intent = getIntent();
        guest = intent.getBooleanExtra("guest", false);
        username = intent.getStringExtra("usr");

        domd = findViewById(R.id.domdText);
        user = findViewById(R.id.user);
        createGame = findViewById(R.id.createGameBtn);

        domd.setText("DÖMD");

        if (guest) {
            user.setText("Welcome guest!");
        } else {
            user.setText("Welcome " + username + "!");
        }

        createGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Alert Title");
        alertDialogBuilder.setMessage("Alert message goes here.");

        // Set a positive button and its click listener
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform an action when the "OK" button is clicked
                // This can be left empty or used for some action
                dialog.dismiss();
            }
        });

        // Create and show the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}