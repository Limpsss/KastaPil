package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class welcome extends AppCompatActivity {
    TextView txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        txt = findViewById(R.id.textView);
        String usr = getIntent().getStringExtra("usr");
        String psw = getIntent().getStringExtra("psw");
        String s = "You entered username: " + usr + " and password: " + psw;
        txt.append(s);
    }
}