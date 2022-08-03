package com.university.chat.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import com.university.chat.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private TextView textViewForgotPassword, textViewSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // instantiate views
        textViewForgotPassword = findViewById(R.id.textView_login_forgot_password);
        textViewSignUp = findViewById(R.id.textView_signUp);

        textViewForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        textViewSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}