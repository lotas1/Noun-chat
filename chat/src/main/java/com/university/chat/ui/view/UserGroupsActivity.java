package com.university.chat.ui.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.university.chat.R;

public class UserGroupsActivity extends AppCompatActivity {
    private Button buttonLogOut;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_groups);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // instantiate view
        buttonLogOut = findViewById(R.id.button_log_out);

        buttonLogOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            mGoogleSignInClient.signOut();
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Intent intent = null;
        //try {
        //    intent = new Intent(UserGroupsActivity.this, Class.forName("com.lotas.nounchat.MainActivity")); // com.university.noungpa.ui.view.MainActivity
        //} catch (ClassNotFoundException e) {
        //    e.printStackTrace();
        //}
        //startActivity(intent);
        //finish();
    }
}