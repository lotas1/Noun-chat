package com.university.chat.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.university.chat.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private TextView textViewForgotPassword, textViewSignUp;
    private AutoCompleteTextView autoCompleteTextViewEmail, autoCompleteTextViewPassword;
    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword;
    private Button buttonLogin, buttonGoogleLogin;
    private String email, password;
    private ProgressDialog progressDialog;

    private ScrollView scrollViewLoginLayout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // instantiate views
        textViewForgotPassword = findViewById(R.id.textView_login_forgot_password);
        textViewSignUp = findViewById(R.id.textView_signUp);
        scrollViewLoginLayout = findViewById(R.id.scrollView_login);

        autoCompleteTextViewEmail = findViewById(R.id.autoCompleteTextview_email_login);
        autoCompleteTextViewPassword = findViewById(R.id.autoCompleteTextview_password_login);

        textInputLayoutEmail = findViewById(R.id.textInputLayout_email_login);
        textInputLayoutPassword = findViewById(R.id.textInputLayout_password_login);

        buttonLogin = findViewById(R.id.button_login);
        buttonGoogleLogin = findViewById(R.id.button_google_login);

        // clear error message in text input layout
        clearError(autoCompleteTextViewEmail, textInputLayoutEmail);
        clearError(autoCompleteTextViewPassword, textInputLayoutPassword);

        // Email login button
        buttonLogin.setOnClickListener(v -> {
            // clear error message in text input layout
            textInputLayoutEmail.setError(null);
            textInputLayoutPassword.setError(null);
            // validate user input
            if (isEmpty(autoCompleteTextViewEmail)){
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

                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing In.......");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    // signing up new users
                    SignIn(email, password);
                }
            }
        });

        buttonGoogleLogin.setOnClickListener(v -> {

        });

        // onClick opens forgot password activity
        textViewForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // onClick opens forgot sign up activity
        textViewSignUp.setOnClickListener(v -> {
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
           // reload();
            Intent intent = new Intent(LoginActivity.this, UserGroupsActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void SignIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();

                        progressDialog.dismiss();

                        Intent intent = new Intent(LoginActivity.this, UserGroupsActivity.class);
                        startActivity(intent);
                        finish();

                        clearTextView(autoCompleteTextViewEmail);
                        clearTextView(autoCompleteTextViewPassword);

                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        progressDialog.dismiss();
                        showSnackBar("There is a problem login in. Check your email and password or create account.");
                        //updateUI(null);
                    }
                });
    }

    // checks if autocomplete text view is empty.
    private boolean isEmpty(AutoCompleteTextView autoCompleteTextView) {
        return TextUtils.isEmpty(autoCompleteTextView.getEditableText());
    }
    // clears textInput layout error
    private void clearError(AutoCompleteTextView autoCompleteTextView, TextInputLayout textInputLayout){
        autoCompleteTextView.setOnClickListener(v -> textInputLayout.setError(null));
    }
    // validate if it is email
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    // clears autocomplete text view.
    private void clearTextView(AutoCompleteTextView autoCompleteTextView){
        autoCompleteTextView.setText(null);
    }
    // show snack bar
    private void showSnackBar(String label){
        Snackbar snackbar = Snackbar.make(scrollViewLoginLayout,label,Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}