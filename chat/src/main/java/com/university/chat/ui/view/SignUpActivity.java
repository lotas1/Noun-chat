package com.university.chat.ui.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.university.chat.R;

public class SignUpActivity extends AppCompatActivity {
    private TextView textViewLogin;
    private AutoCompleteTextView autoCompleteTextViewUsername, autoCompleteTextViewEmail, autoCompleteTextViewPassword;
    private TextInputLayout textInputLayoutUsername, textInputLayoutEmail, textInputLayoutPassword;
    private Button buttonSignUp, buttonGoogleSignUp;

    private FirebaseAuth mAuth;
    private String username, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // instantiate views
        textViewLogin = findViewById(R.id.textView_login);
        autoCompleteTextViewUsername = findViewById(R.id.autoCompleteTextview_username_signUp);
        autoCompleteTextViewEmail = findViewById(R.id.autoCompleteTextview_email_signUp);
        autoCompleteTextViewPassword = findViewById(R.id.autoCompleteTextview_password_signUp);

        textInputLayoutUsername = findViewById(R.id.textInputLayout_username_signup);
        textInputLayoutEmail = findViewById(R.id.textInputLayout_email_signup);
        textInputLayoutPassword = findViewById(R.id.textInputLayout_password_signup);

        buttonSignUp = findViewById(R.id.button_signUp);
        buttonGoogleSignUp = findViewById(R.id.button_google_signUp);


        // clear error message in text input layout
        clearError(autoCompleteTextViewUsername, textInputLayoutUsername);
        clearError(autoCompleteTextViewEmail, textInputLayoutEmail);
        clearError(autoCompleteTextViewPassword, textInputLayoutPassword);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        buttonSignUp.setOnClickListener(v -> {
            if (isEmpty(autoCompleteTextViewUsername)){
                textInputLayoutUsername.setError("Enter username");
            }else if (isEmpty(autoCompleteTextViewEmail)){
                textInputLayoutEmail.setError("Enter email");
            } else if (isEmpty(autoCompleteTextViewPassword)) {
                textInputLayoutPassword.setError("Password must be at least 8 characters");
            } else {
                // validate email
                if (!isValidEmail(autoCompleteTextViewEmail.getText().toString())){
                    textInputLayoutEmail.setError("Enter valid email");
                }else {

                }
            }
        });
        textViewLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
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
        }
    }

    private boolean isEmpty(AutoCompleteTextView autoCompleteTextView) {
        return TextUtils.isEmpty(autoCompleteTextView.getEditableText());
    }
    private void clearError(AutoCompleteTextView autoCompleteTextView, TextInputLayout textInputLayout){
        autoCompleteTextView.setOnClickListener(v -> textInputLayout.setError(null));
    }
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}