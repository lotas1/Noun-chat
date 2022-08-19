package com.lotas.nounchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.university.chat.ui.view.GroupMemberActivity;
import com.university.chat.ui.view.authentication.SignUpIntroActivity;

public class MainActivity extends AppCompatActivity {
    private Button buttonUser, buttonAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // code begins
        // open user module
        //Intent intent = new Intent(this, SignUpIntroActivity.class);
        //startActivity(intent);

        buttonUser = findViewById(R.id.buttonUser);
        buttonAdmin = findViewById(R.id.buttonAdmin);

        buttonUser.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, SignUpIntroActivity.class);
            startActivity(intent1);
        });

        // admin
        buttonAdmin.setOnClickListener(v -> {
            //Intent intent1 = new Intent(this, GroupMemberActivity.class);
            //startActivity(intent1);
        });
    }
}