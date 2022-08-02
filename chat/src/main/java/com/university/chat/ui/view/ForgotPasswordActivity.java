package com.university.chat.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.university.chat.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // instantiate views
        toolbar = findViewById(R.id.toolbar_forgot_password);

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }
}