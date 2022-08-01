package com.lotas.nounchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.university.chat.ui.view.SignUpIntroActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // code begins
        // open user module
        Intent intent = new Intent(this, SignUpIntroActivity.class);
        startActivity(intent);
    }
}