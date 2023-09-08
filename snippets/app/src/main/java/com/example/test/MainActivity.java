package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    Button btn;
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.usernameText);
        password = findViewById(R.id.passwordText);
        btn = findViewById(R.id.log_in);

        btn.setOnClickListener(view -> logIn(username.getText().toString(), password.getText().toString()));
    }

    private void logIn(String usr, String psw) {
        Intent in = new Intent(this, welcome.class);
        in.putExtra("usr", usr);
        in.putExtra("psw", psw);
        startActivity(in);
    }

}