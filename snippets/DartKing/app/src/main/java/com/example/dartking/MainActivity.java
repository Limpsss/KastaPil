package com.example.dartking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    private Button guest;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createWidgets();
    }

    private void createWidgets() {
        btn = findViewById(R.id.button);
        guest = findViewById(R.id.guest);
        username = findViewById(R.id.usrText);
        password = findViewById(R.id.pswText);

        btn.setText("Log in");
        guest.setText("Log in as guest");
        username.setHint("Username");
        password.setHint("Password");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn(username.getText().toString(), password.getText().toString());
            }
        });

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainPage.class);
                intent.putExtra("guest", true);
                startActivity(intent);
            }
        });
    }

    private void logIn(String username, String password) {
        String correctPassword = "bajs";
        boolean guest = false;

        if (password.equals(correctPassword)) {
            Intent intent = new Intent(MainActivity.this, MainPage.class);
            intent.putExtra("usr", username);
            intent.putExtra("guest", guest);
            startActivity(intent);
        } else {
            // Password is wrong, show error message
            TextView errorMessage = findViewById(R.id.errorMessage);
            errorMessage.setText("Incorrect password. Please try again.");
            errorMessage.setVisibility(View.VISIBLE);
        }
    }
}