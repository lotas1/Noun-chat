package com.university.chat.ui.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.university.chat.R;

public class SignUpIntroActivity extends AppCompatActivity {
    private Button buttonGetStarted;
    private TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_intro);

        // instantiate views
        buttonGetStarted = findViewById(R.id.button_signUp_getStarted);
        textViewLogin = findViewById(R.id.textView_login);

        textViewLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        buttonGetStarted.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}