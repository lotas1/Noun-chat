package com.university.chat.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
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
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

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
                }else if (autoCompleteTextViewPassword.getText().toString().length() < 8){
                    textInputLayoutPassword.setError("Password must be at least 8 characters");
                }else {
                    // assign value to email and password variable
                    email = autoCompleteTextViewEmail.getText().toString().trim();
                    password = autoCompleteTextViewPassword.getText().toString().trim();

                    // signing up new users
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        //Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        Toast.makeText(SignUpActivity.this, "Authentication successful.",
                                                Toast.LENGTH_SHORT).show();

                                        clearTextView(autoCompleteTextViewUsername);
                                        clearTextView(autoCompleteTextViewEmail);
                                        clearTextView(autoCompleteTextViewPassword);
                                        //updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }
                                }
                            });
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
    private void clearTextView(AutoCompleteTextView autoCompleteTextView){
        autoCompleteTextView.setText(null);
    }
}