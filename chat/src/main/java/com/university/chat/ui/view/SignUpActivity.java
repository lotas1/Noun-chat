package com.university.chat.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.university.chat.R;

public class SignUpActivity extends AppCompatActivity {
    //private TextView textViewLogin;
    private AutoCompleteTextView autoCompleteTextViewUsername, autoCompleteTextViewEmail, autoCompleteTextViewPassword, autoCompleteTextViewPassword2;
    private TextInputLayout textInputLayoutUsername, textInputLayoutEmail, textInputLayoutPassword, textInputLayoutPassword2;
    private Button buttonSignUp, buttonGoogleSignUp;
    private ScrollView scrollViewSignUpLayout;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private String username, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // instantiate views
        //textViewLogin = findViewById(R.id.textView_login);
        scrollViewSignUpLayout = findViewById(R.id.scrollView_signUp);
        autoCompleteTextViewUsername = findViewById(R.id.autoCompleteTextview_username_signUp);
        autoCompleteTextViewEmail = findViewById(R.id.autoCompleteTextview_email_signUp);
        autoCompleteTextViewPassword = findViewById(R.id.autoCompleteTextview_password_signUp);
        autoCompleteTextViewPassword2 = findViewById(R.id.autoCompleteTextview_password2_signUp);

        textInputLayoutUsername = findViewById(R.id.textInputLayout_username_signup);
        textInputLayoutEmail = findViewById(R.id.textInputLayout_email_signup);
        textInputLayoutPassword = findViewById(R.id.textInputLayout_password_signup);
        textInputLayoutPassword2 = findViewById(R.id.textInputLayout_password2_signup);

        buttonSignUp = findViewById(R.id.button_signUp);
        buttonGoogleSignUp = findViewById(R.id.button_google_signUp);


        // clear error message in text input layout
        clearError(autoCompleteTextViewUsername, textInputLayoutUsername);
        clearError(autoCompleteTextViewEmail, textInputLayoutEmail);
        clearError(autoCompleteTextViewPassword, textInputLayoutPassword);
        clearError(autoCompleteTextViewPassword2, textInputLayoutPassword2);

        buttonSignUp.setOnClickListener(v -> {
            // clear error message in text input layout
            textInputLayoutUsername.setError(null);
            textInputLayoutEmail.setError(null);
            textInputLayoutPassword.setError(null);
            textInputLayoutPassword2.setError(null);
            // validate user input
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
                } else if (!autoCompleteTextViewPassword.getText().toString().equals(autoCompleteTextViewPassword2.getText().toString())) {
                    textInputLayoutPassword2.setError("Password does not match");
                }else {
                    // assign value to email and password variable
                    email = autoCompleteTextViewEmail.getText().toString().trim();
                    password = autoCompleteTextViewPassword.getText().toString().trim();

                    progressDialog = new ProgressDialog(SignUpActivity.this);
                    progressDialog.setMessage("Signing In.......");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    // signing up new users
                    createAccount(email, password);
                }
            }
        });
        /**textViewLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });**/
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //reload();
            Intent intent = new Intent(SignUpActivity.this, UserGroupsActivity.class);
            startActivity(intent);
            finish();
        }
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
        Snackbar snackbar = Snackbar.make(scrollViewSignUpLayout,label,Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(getResources().getColor(com.university.theme.R.color.primaryColor));
        snackbar.show();
    }
    // method for creating account
    private void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();

                        progressDialog.dismiss();

                        Intent intent = new Intent(SignUpActivity.this, UserGroupsActivity.class);
                        startActivity(intent);
                        finish();

                        clearTextView(autoCompleteTextViewUsername);
                        clearTextView(autoCompleteTextViewEmail);
                        clearTextView(autoCompleteTextViewPassword);
                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        progressDialog.dismiss();
                        showSnackBar("There is a problem creating your account. Check your email is spelled correctly or previously used to create an account already");
                        //updateUI(null);
                    }
                });
    }
}