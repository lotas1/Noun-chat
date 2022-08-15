package com.university.chat.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.university.chat.R;

public class GeneralChatActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private LinearLayout linearLayoutUser, linearLayoutAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_chat);

        // instantiate views
        toolbar = findViewById(R.id.toolbar_general_chat);
        linearLayoutUser = findViewById(R.id.linearLayout_user_generalChat);
        linearLayoutAdmin = findViewById(R.id.linearLayout_adminOnly_generalChat);
        // close activity on click
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        // instance of bundle
        Bundle b = getIntent().getExtras();
        String transitionName = b.getString("transitionName");
        //imageViewFullDisplay.setTransitionName(transitionName);

        // set toolbar title
        toolbar.setTitle(b.getString("groupName"));

        // variables holds value for is admin only
        boolean isAdminOnly = b.getBoolean("adminOnly");
        // checks if isAminOnly or not
        if (isAdminOnly){
            linearLayoutUser.setVisibility(View.GONE);
            linearLayoutAdmin.setVisibility(View.VISIBLE);
        }else {
            linearLayoutUser.setVisibility(View.VISIBLE);
            linearLayoutAdmin.setVisibility(View.GONE);
        }
    }
}