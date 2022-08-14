package com.university.chat.ui.view.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.university.chat.R;
import com.university.chat.ui.view.UserGroupsActivity;

public class SignUpIntroActivity extends AppCompatActivity {
    private Button buttonGetStarted;
    private TextView textViewLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_intro);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

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

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //reload();
            Intent intent = new Intent(SignUpIntroActivity.this, UserGroupsActivity.class);
            startActivity(intent);
            finish();
        }
    }
}